begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.mina
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mina
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ContextTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Endpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Producer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|common
operator|.
name|IoSession
import|;
end_import

begin_comment
comment|/**  * For unit testing the<tt>logger</tt> option.  */
end_comment

begin_class
DECL|class|MinaLoggerOptionTest
specifier|public
class|class
name|MinaLoggerOptionTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testLoggerOptionTrue ()
specifier|public
name|void
name|testLoggerOptionTrue
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|uri
init|=
literal|"mina:tcp://localhost:6321?textline=true&minaLogger=true"
decl_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
name|uri
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|this
operator|.
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|Producer
name|producer
init|=
name|endpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// set input and execute it
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|Field
name|field
init|=
name|producer
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredField
argument_list|(
literal|"session"
argument_list|)
decl_stmt|;
name|field
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|IoSession
name|session
init|=
operator|(
name|IoSession
operator|)
name|field
operator|.
name|get
argument_list|(
name|producer
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"There should be a logger filter"
argument_list|,
name|session
operator|.
name|getFilterChain
argument_list|()
operator|.
name|contains
argument_list|(
literal|"logger"
argument_list|)
argument_list|)
expr_stmt|;
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertMockEndpointsSatisifed
argument_list|()
expr_stmt|;
block|}
DECL|method|testLoggerOptionFalse ()
specifier|public
name|void
name|testLoggerOptionFalse
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|uri
init|=
literal|"mina:tcp://localhost:6321?textline=true&minaLogger=false"
decl_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
name|uri
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|this
operator|.
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|Producer
name|producer
init|=
name|endpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// set input and execute it
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|Field
name|field
init|=
name|producer
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredField
argument_list|(
literal|"session"
argument_list|)
decl_stmt|;
name|field
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|IoSession
name|session
init|=
operator|(
name|IoSession
operator|)
name|field
operator|.
name|get
argument_list|(
name|producer
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"There should NOT be a logger filter"
argument_list|,
name|session
operator|.
name|getFilterChain
argument_list|()
operator|.
name|contains
argument_list|(
literal|"logger"
argument_list|)
argument_list|)
expr_stmt|;
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertMockEndpointsSatisifed
argument_list|()
expr_stmt|;
block|}
DECL|method|testNoLoggerOption ()
specifier|public
name|void
name|testNoLoggerOption
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|uri
init|=
literal|"mina:tcp://localhost:6321?textline=true"
decl_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
name|uri
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|this
operator|.
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|Producer
name|producer
init|=
name|endpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// set input and execute it
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|Field
name|field
init|=
name|producer
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredField
argument_list|(
literal|"session"
argument_list|)
decl_stmt|;
name|field
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|IoSession
name|session
init|=
operator|(
name|IoSession
operator|)
name|field
operator|.
name|get
argument_list|(
name|producer
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"There should NOT default be a logger filter"
argument_list|,
name|session
operator|.
name|getFilterChain
argument_list|()
operator|.
name|contains
argument_list|(
literal|"logger"
argument_list|)
argument_list|)
expr_stmt|;
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertMockEndpointsSatisifed
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

