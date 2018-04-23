package com.example.jbox2d.jbox2dmaster.collision

import android.view.View
import android.view.ViewGroup
import com.example.jbox2d.jbox2dmaster.R
import org.jbox2d.collision.shapes.CircleShape
import org.jbox2d.collision.shapes.PolygonShape
import org.jbox2d.collision.shapes.Shape
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.*
import java.util.*

/**
 * @Author:  Ycl
 * @Date:  2018-04-23 14:38
 * @Desc:
 */
class JBCollisionImpl(private val viewGroup: ViewGroup) {

    lateinit var world: World
    private val random = Random()

    private val dt = 1f / 60f
    private val velocityIterations = 3
    private val positionIterations = 10

    private var friction = 0.3f
    private var density = 0.5f
    private var restitution = 0.3f
    private var ratio = 50f

    private var width: Int = 0
    private var height: Int = 0


    init {
        density = viewGroup.context.resources.displayMetrics.density

    }

    fun onDraw() {
        // 开启模拟世界
        if (null != world) {
            world.step(dt, velocityIterations, positionIterations)
        }

        val childCount = viewGroup.getChildCount()
        for (i in 0 until childCount) {
            val view = viewGroup.getChildAt(i)
            val body = view.getTag(R.id.jb_body_tag) as Body
            if (body != null) {
                view.setX(metersToPixels(body.position.x) - view.getWidth() / 2)
                view.setY(metersToPixels(body.position.y) - view.getHeight() / 2)
                view.setRotation((body.angle / 3.14f * 180f) % 360)// 弧度变角度
            }
        }
        viewGroup.invalidate()
    }

    fun onLayout(changed: Boolean) {
        createWorld(changed)

    }

    fun onSizeChanged(w: Int, h: Int) {
        this.width = w
        this.height = h
    }

    fun onSensorChanged(x: Float, y: Float) {
        val childCount = viewGroup.childCount
        for (i in 0 until childCount) {
            val view = viewGroup.getChildAt(i)
            val body = view.getTag(R.id.jb_body_tag) as Body
            body.applyLinearImpulse(Vec2(x, y), body.position, true)
        }

    }

    fun onRandomChanged() {


    }

    // 创建模拟世界
    private fun createWorld(changed: Boolean) {
        if (world == null) {
            // 定义世界  创建矢量方向
            world = World(Vec2(0f, 10.0f))
            //创建四个方向的静态刚体，让其能够有弹性
            createTopAndBottomBounds()
            createLeftAndRightBounds()
        }
        val childCount = viewGroup.childCount
        for (i in 0 until childCount) {
            val view = viewGroup.getChildAt(i)
            val body = view.getTag(R.id.jb_body_tag) as Body
            if (body == null || changed) { // tag==null代表未创建刚体,如果已经绑定了就代表已经创建了刚体
                createBody(view)
            }
        }
    }


    private fun createTopAndBottomBounds() {
        // 刚体
        val bodyDef = BodyDef()
        bodyDef.type = BodyType.STATIC

        //矩形
        val box = PolygonShape()
        val boxWidth = pixelsToMeters(width.toFloat())
        val boxHeight = pixelsToMeters(ratio)  // 1
        box.setAsBox(boxWidth, boxHeight)

        val fixtureDef = FixtureDef()
        fixtureDef.shape = box
        fixtureDef.density = 0.5f
        fixtureDef.friction = 0.3f// 摩擦系数
        fixtureDef.restitution = 0.5f // 补偿系数

        bodyDef.position.set(0f, -boxHeight)// 底部弹力墙  -1
        val topBody = world.createBody(bodyDef)
        topBody.createFixture(fixtureDef)// 设置材料系数不同 反弹的不同，铅球，像皮球区别

        bodyDef.position.set(0f, pixelsToMeters(height.toFloat()) + boxHeight)
        val bottomBody = world.createBody(bodyDef)
        bottomBody.createFixture(fixtureDef)
    }

    private fun createLeftAndRightBounds() {
        val boxWidth = pixelsToMeters(ratio)
        val boxHeight = pixelsToMeters(height.toFloat())

        val bodyDef = BodyDef()
        bodyDef.type = BodyType.STATIC

        val box = PolygonShape()
        box.setAsBox(boxWidth, boxHeight)
        val fixtureDef = FixtureDef()
        fixtureDef.shape = box
        fixtureDef.density = 0.5f
        fixtureDef.friction = 0.3f
        fixtureDef.restitution = 0.5f

        bodyDef.position.set(-boxWidth, 0f)
        val leftBody = world.createBody(bodyDef)
        leftBody.createFixture(fixtureDef)


        bodyDef.position.set(pixelsToMeters(width.toFloat()) + boxWidth, 0f)
        val rightBody = world.createBody(bodyDef)
        rightBody.createFixture(fixtureDef)
    }


    private fun metersToPixels(meters: Float): Float {
        return meters * ratio
    }

    private fun pixelsToMeters(pixels: Float): Float {
        return pixels / ratio
    }


    private fun createBody(view: View) {
        val bodyDef = BodyDef()
        bodyDef.setType(BodyType.DYNAMIC) // 设置动态

        // 计算中心
        bodyDef.position.set(pixelsToMeters(view.x + view.width / 2),
                pixelsToMeters(view.y + view.height / 2))
        var shape: Shape? = null
        val isCircle = view.getTag(R.id.jb_view_circle_tag) as Boolean
        if (isCircle != null && isCircle) {
            shape = createCircleShape(view)
        } else {
            shape = createPolygonShape(view)
        }
        val fixture = FixtureDef()
        fixture.setShape(shape)
        fixture.friction = friction
        fixture.restitution = restitution
        fixture.density = density

        val body = world.createBody(bodyDef)
        body.createFixture(fixture)


        view.setTag(R.id.jb_body_tag, body)
        // 设置一个线性运动
        body.linearVelocity = Vec2(random.nextFloat(), random.nextFloat())
    }

    private fun createCircleShape(view: View): Shape {
        val circleShape = CircleShape()
        circleShape.radius = pixelsToMeters((view.width / 2).toFloat())
        return circleShape
    }

    private fun createPolygonShape(view: View): Shape {
        val polygonShape = PolygonShape()
        polygonShape.setAsBox(pixelsToMeters((view.width / 2).toFloat()), pixelsToMeters((view.height / 2).toFloat()))
        return polygonShape
    }
}