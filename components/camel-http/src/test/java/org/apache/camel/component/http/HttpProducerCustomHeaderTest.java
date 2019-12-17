begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http
package|;
end_package

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
name|component
operator|.
name|http
operator|.
name|handler
operator|.
name|HeaderValidationHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|bootstrap
operator|.
name|HttpServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|bootstrap
operator|.
name|ServerBootstrap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpHeaders
operator|.
name|HOST
import|;
end_import

begin_class
DECL|class|HttpProducerCustomHeaderTest
specifier|public
class|class
name|HttpProducerCustomHeaderTest
extends|extends
name|BaseHttpTest
block|{
DECL|field|CUSTOM_HOST
specifier|private
specifier|static
specifier|final
name|String
name|CUSTOM_HOST
init|=
literal|"test"
decl_stmt|;
DECL|field|localServer
specifier|private
name|HttpServer
name|localServer
decl_stmt|;
annotation|@
name|Before
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|expectedHeaders
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|expectedHeaders
operator|.
name|put
argument_list|(
name|HOST
argument_list|,
name|CUSTOM_HOST
argument_list|)
expr_stmt|;
name|localServer
operator|=
name|ServerBootstrap
operator|.
name|bootstrap
argument_list|()
operator|.
name|setHttpProcessor
argument_list|(
name|getBasicHttpProcessor
argument_list|()
argument_list|)
operator|.
name|setConnectionReuseStrategy
argument_list|(
name|getConnectionReuseStrategy
argument_list|()
argument_list|)
operator|.
name|setResponseFactory
argument_list|(
name|getHttpResponseFactory
argument_list|()
argument_list|)
operator|.
name|setExpectationVerifier
argument_list|(
name|getHttpExpectationVerifier
argument_list|()
argument_list|)
operator|.
name|setSslContext
argument_list|(
name|getSSLContext
argument_list|()
argument_list|)
operator|.
name|registerHandler
argument_list|(
literal|"*"
argument_list|,
operator|new
name|HeaderValidationHandler
argument_list|(
literal|"GET"
argument_list|,
literal|"customHostHeader="
operator|+
name|CUSTOM_HOST
argument_list|,
literal|null
argument_list|,
name|getExpectedContent
argument_list|()
argument_list|,
name|expectedHeaders
argument_list|)
argument_list|)
operator|.
name|registerHandler
argument_list|(
literal|"*"
argument_list|,
operator|new
name|HeaderValidationHandler
argument_list|(
literal|"GET"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|getExpectedContent
argument_list|()
argument_list|,
literal|null
argument_list|)
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
name|localServer
operator|.
name|start
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
annotation|@
name|Override
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
if|if
condition|(
name|localServer
operator|!=
literal|null
condition|)
block|{
name|localServer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testHttpProducerGivenCustomHostHeaderQuerySetCustomHost ()
specifier|public
name|void
name|testHttpProducerGivenCustomHostHeaderQuerySetCustomHost
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpComponent
name|component
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"http"
argument_list|,
name|HttpComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|component
operator|.
name|setConnectionTimeToLive
argument_list|(
literal|1000L
argument_list|)
expr_stmt|;
name|HttpEndpoint
name|endpoint
init|=
operator|(
name|HttpEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"http://"
operator|+
name|localServer
operator|.
name|getInetAddress
argument_list|()
operator|.
name|getHostName
argument_list|()
operator|+
literal|":"
operator|+
name|localServer
operator|.
name|getLocalPort
argument_list|()
operator|+
literal|"/myget?customHostHeader="
operator|+
name|CUSTOM_HOST
argument_list|)
decl_stmt|;
name|HttpProducer
name|producer
init|=
operator|new
name|HttpProducer
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|producer
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHttpProducerGivenEmptyQueryShouldNotSetCustomHost ()
specifier|public
name|void
name|testHttpProducerGivenEmptyQueryShouldNotSetCustomHost
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpComponent
name|component
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"http"
argument_list|,
name|HttpComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|component
operator|.
name|setConnectionTimeToLive
argument_list|(
literal|1000L
argument_list|)
expr_stmt|;
name|HttpEndpoint
name|endpoint
init|=
operator|(
name|HttpEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"http://"
operator|+
name|localServer
operator|.
name|getInetAddress
argument_list|()
operator|.
name|getHostName
argument_list|()
operator|+
literal|":"
operator|+
name|localServer
operator|.
name|getLocalPort
argument_list|()
operator|+
literal|"/myget"
argument_list|)
decl_stmt|;
name|HttpProducer
name|producer
init|=
operator|new
name|HttpProducer
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|producer
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

