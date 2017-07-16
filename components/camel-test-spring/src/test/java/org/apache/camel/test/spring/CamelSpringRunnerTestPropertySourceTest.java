begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.test.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|spring
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
name|EndpointInject
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
name|Produce
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
name|ProducerTemplate
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
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

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
name|BootstrapWith
import|;
end_import

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
name|TestPropertySource
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|CamelSpringRunner
operator|.
name|class
argument_list|)
annotation|@
name|BootstrapWith
argument_list|(
name|CamelTestContextBootstrapper
operator|.
name|class
argument_list|)
annotation|@
name|ContextConfiguration
annotation|@
name|TestPropertySource
argument_list|(
name|properties
operator|=
literal|"fixedBody=Camel"
argument_list|)
DECL|class|CamelSpringRunnerTestPropertySourceTest
specifier|public
class|class
name|CamelSpringRunnerTestPropertySourceTest
block|{
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:in"
argument_list|)
DECL|field|start
specifier|private
name|ProducerTemplate
name|start
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:out"
argument_list|)
DECL|field|end
specifier|private
name|MockEndpoint
name|end
decl_stmt|;
annotation|@
name|Test
DECL|method|readsFileAndInlinedPropertiesFromAnnotation ()
specifier|public
name|void
name|readsFileAndInlinedPropertiesFromAnnotation
parameter_list|()
throws|throws
name|Exception
block|{
name|end
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Camel"
argument_list|)
expr_stmt|;
name|start
operator|.
name|sendBody
argument_list|(
literal|"Aardvark"
argument_list|)
expr_stmt|;
name|end
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

