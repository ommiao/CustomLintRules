package com.example.checks.accessibility

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Detector.UastScanner
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import org.jetbrains.uast.*

@Suppress("UnstableApiUsage")
class AccessibilityUsageDetector : Detector(), UastScanner {
    override fun getApplicableUastTypes(): List<Class<out UElement?>> {
        return listOf(
//            ULiteralExpression::class.java,
//            UExpression::class.java,
//            UMethod::class.java,
            UCallExpression::class.java,
//            UCallableReferenceExpression::class.java,
//            UParameter::class.java,
//            ULabeledExpression::class.java,
//            UElement::class.java,
        )
    }

    override fun createUastHandler(context: JavaContext): UElementHandler {
        return object : UElementHandler() {
//            override fun visitLiteralExpression(node: ULiteralExpression) {
//                context.report(
//                    ISSUE, node, context.getLocation(node),
//                    "LiteralExpression"
//                )
//            }

//            override fun visitExpression(node: UExpression) {
//                context.report(
//                    ISSUE, node, context.getLocation(node),
//                    "Expression"
//                )
//            }

//            override fun visitMethod(node: UMethod) {
//                context.report(
//                    ISSUE, node, context.getLocation(node),
//                    "Method"
//                )
//            }

            override fun visitCallExpression(node: UCallExpression) {
                if (node.methodName == "Icon" || node.methodName == "Image") {
                    node.valueArguments.forEach {
                        println("${node.getParameterForArgument(it)?.name} = ${it.asSourceString()}")
                    }
                    context.report(
                        ISSUE, node, context.getLocation(node),
                        "UCallExpression"
                    )
                }
            }

//            override fun visitCallableReferenceExpression(node: UCallableReferenceExpression) {
//                context.report(
//                    ISSUE, node, context.getLocation(node),
//                    "UCallableReferenceExpression"
//                )
//            }

//            override fun visitParameter(node: UParameter) {
//                context.report(
//                    ISSUE, node as UElement, context.getLocation(node as UElement),
//                    "UParameter"
//                )
//            }

//            override fun visitLabeledExpression(node: ULabeledExpression) {
//                context.report(
//                    ISSUE, node, context.getLocation(node),
//                    "ULabeledExpression"
//                )
//            }

//            override fun visitElement(node: UElement) {
//                context.report(
//                    ISSUE, node, context.getLocation(node),
//                    "UElement"
//                )
//            }

        }
    }

    companion object {
        @JvmField
        val ISSUE: Issue = Issue.create(
            id = "InvalidAccessibilityDescription",
            briefDescription = "Accessibility description is null or empty.",
            explanation = """
                    Use compose **Image** or **Icon** widget, but NOT given a valid accessibility description.
                    """,
            category = Category.CORRECTNESS,
            priority = 6,
            severity = Severity.ERROR,
            implementation = Implementation(
                AccessibilityUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }
}
