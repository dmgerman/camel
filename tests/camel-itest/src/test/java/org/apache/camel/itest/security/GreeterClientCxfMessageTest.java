begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.itest.security
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|security
package|;
end_package

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_class
annotation|@
name|ContextConfiguration
argument_list|(
name|locations
operator|=
block|{
literal|"CxfMessageCamelContext.xml"
block|}
argument_list|)
DECL|class|GreeterClientCxfMessageTest
specifier|public
class|class
name|GreeterClientCxfMessageTest
extends|extends
name|GreeterClientTest
block|{  }
end_class

end_unit

