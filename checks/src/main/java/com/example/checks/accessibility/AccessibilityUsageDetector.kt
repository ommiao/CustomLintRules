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
import com.intellij.psi.PsiParameter
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
                    var contentDescriptionParameter: PsiParameter? = null
                    var contentDescriptionValue: UExpression? = null
                    node.valueArguments.forEach {
                        val parameter = node.getParameterForArgument(it)
                        println("${parameter?.name} = ${it.asSourceString()}")
                        if (parameter?.name == "contentDescription") {
                            contentDescriptionParameter = parameter
                            contentDescriptionValue = it
                        }
                    }
                    if (contentDescriptionParameter != null) {
                        val asSourceString = contentDescriptionValue?.asSourceString() ?: ""
                        var lintDescription: String? = null
                        if (asSourceString == "null") {
                            lintDescription = "contentDescription can't be null"
                        } else if ("\"[ ]*\"".toRegex().matches(asSourceString)) {
                            lintDescription = "contentDescription can't be blank"
                        }
                        if (lintDescription != null) {
                            context.report(
                                ISSUE, node, context.getLocation(node),
                                lintDescription
                            )
                        }
                    }
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
