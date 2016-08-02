begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.itest.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|springboot
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
name|ConditionalOnResource
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
name|ImportResource
import|;
end_import

begin_comment
comment|/**  * Loads 'META-INF/spring/spring.xml' if present.  */
end_comment

begin_class
annotation|@
name|Configuration
annotation|@
name|ConditionalOnResource
argument_list|(
name|resources
operator|=
literal|"META-INF/spring/spring.xml"
argument_list|)
annotation|@
name|ImportResource
argument_list|(
literal|"META-INF/spring/spring.xml"
argument_list|)
DECL|class|ITestXmlConfiguration
specifier|public
class|class
name|ITestXmlConfiguration
block|{ }
end_class

end_unit

