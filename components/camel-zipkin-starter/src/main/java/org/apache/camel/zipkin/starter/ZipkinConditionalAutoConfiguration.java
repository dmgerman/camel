begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.zipkin.starter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|zipkin
operator|.
name|starter
package|;
end_package

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|condition
operator|.
name|ConditionalOnProperty
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Import
import|;
end_import

begin_comment
comment|/**  * A configuration controller to enable Zipkin via the configuration property.  * Useful to bootstrap Zipkin when not using the {@link CamelZipkin} annotation.  */
end_comment

begin_class
annotation|@
name|Configuration
annotation|@
name|ConditionalOnProperty
argument_list|(
name|value
operator|=
literal|"camel.zipkin.enabled"
argument_list|)
annotation|@
name|Import
argument_list|(
name|ZipkinAutoConfiguration
operator|.
name|class
argument_list|)
DECL|class|ZipkinConditionalAutoConfiguration
specifier|public
class|class
name|ZipkinConditionalAutoConfiguration
block|{ }
end_class

end_unit

