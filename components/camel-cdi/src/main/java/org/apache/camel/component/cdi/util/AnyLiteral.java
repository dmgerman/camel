begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.cdi.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cdi
operator|.
name|util
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|Any
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|util
operator|.
name|AnnotationLiteral
import|;
end_import

begin_class
DECL|class|AnyLiteral
specifier|public
class|class
name|AnyLiteral
extends|extends
name|AnnotationLiteral
argument_list|<
name|Any
argument_list|>
implements|implements
name|Any
block|{ }
end_class

end_unit

