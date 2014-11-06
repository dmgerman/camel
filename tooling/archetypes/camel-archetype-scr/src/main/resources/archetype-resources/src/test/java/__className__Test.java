begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_expr_stmt
unit|#
name|set
argument_list|(
name|$symbol_pound
operator|=
literal|'#'
argument_list|)
expr|#
name|set
argument_list|(
name|$symbol_dollar
operator|=
literal|'$'
argument_list|)
expr|#
name|set
argument_list|(
name|$symbol_escape
operator|=
literal|'\' )
expr|#
operator|#
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
expr|#
operator|#
name|Licensed
name|to
name|the
name|Apache
name|Software
name|Foundation
argument_list|(
name|ASF
argument_list|)
name|under
name|one
name|or
name|more
expr|#
operator|#
name|contributor
name|license
name|agreements
operator|.
name|See
name|the
name|NOTICE
name|file
name|distributed
name|with
expr|#
operator|#
name|this
name|work
for|for additional information regarding copyright ownership. ## The ASF licenses this file to You under the Apache License
operator|,
name|Version
literal|2.0
expr|#
operator|#
operator|(
name|the
literal|"License"
operator|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|you
name|may
name|not
name|use
name|this
name|file
name|except
name|in
name|compliance
name|with
expr|#
operator|#
name|the
name|License
operator|.
name|You
name|may
name|obtain
name|a
name|copy
name|of
name|the
name|License
name|at
expr|#
operator|#
expr|#
operator|#
name|http
operator|:
comment|//www.apache.org/licenses/LICENSE-2.0
expr|#
operator|#
expr|#
operator|#
name|Unless
name|required
name|by
name|applicable
name|law
name|or
name|agreed
name|to
name|in
name|writing
operator|,
name|software
expr|#
operator|#
name|distributed
name|under
name|the
name|License
name|is
name|distributed
name|on
name|an
literal|"AS IS"
name|BASIS
operator|,
expr_stmt|#
operator|#
name|WITHOUT
name|WARRANTIES
name|OR
name|CONDITIONS
name|OF
name|ANY
name|KIND
operator|,
name|either
name|express
name|or
name|implied
operator|.
expr|#
operator|#
name|See
name|the
name|License
end_expr_stmt

begin_for
for|for the specific language governing permissions and ## limitations under the License. ## ------------------------------------------------------------------------
comment|// This file was generated from ${archetypeGroupId}/${archetypeArtifactId}/${archetypeVersion}
for|package $
block|{
name|groupId
block|}
end_for

begin_empty_stmt
empty_stmt|;
end_empty_stmt

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|scr
operator|.
name|ScrHelper
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
name|AdviceWithRouteBuilder
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
name|MockComponent
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
name|camel
operator|.
name|model
operator|.
name|ModelCamelContext
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
name|model
operator|.
name|RouteDefinition
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|io
operator|.
name|IOUtils
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
name|Rule
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
name|rules
operator|.
name|TestName
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
name|junit
operator|.
name|runners
operator|.
name|JUnit4
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|jayway
operator|.
name|restassured
operator|.
name|RestAssured
operator|.
name|*
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|jayway
operator|.
name|restassured
operator|.
name|matcher
operator|.
name|RestAssuredMatchers
operator|.
name|*
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|*
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|JUnit4
operator|.
name|class
argument_list|)
DECL|class|$
specifier|public
class|class
name|$
block|{
name|className
block|}
end_class

begin_expr_stmt
DECL|class|$
name|Test
block|{
name|Logger
name|log
operator|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
block|;      @
name|Rule
specifier|public
name|TestName
name|testName
operator|=
operator|new
name|TestName
argument_list|()
block|;
name|$
block|{
name|className
block|}
name|integration
block|;
name|ModelCamelContext
name|context
block|;      @
name|Before
specifier|public
name|void
name|setUp
argument_list|()
throws|throws
name|Exception
block|{
name|log
operator|.
name|info
argument_list|(
literal|"*******************************************************************"
argument_list|)
block|;
name|log
operator|.
name|info
argument_list|(
literal|"Test: "
operator|+
name|testName
operator|.
name|getMethodName
argument_list|()
argument_list|)
block|;
name|log
operator|.
name|info
argument_list|(
literal|"*******************************************************************"
argument_list|)
block|;
comment|// Set property prefix for unit testing
name|System
operator|.
name|setProperty
argument_list|(
name|$
block|{
name|className
block|}
operator|.
name|PROPERTY_PREFIX
argument_list|,
literal|"unit"
argument_list|)
block|;
comment|// Prepare the integration
name|integration
operator|=
operator|new
name|$
block|{
name|className
block|}
operator|(
operator|)
block|;
name|integration
operator|.
name|prepare
argument_list|(
literal|null
argument_list|,
name|ScrHelper
operator|.
name|getScrProperties
argument_list|(
name|integration
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
block|;
name|context
operator|=
name|integration
operator|.
name|getContext
argument_list|()
block|;
comment|// Fake a component for test
comment|// context.addComponent("amq", new MockComponent());
block|}
expr|@
name|After
specifier|public
name|void
name|tearDown
argument_list|()
throws|throws
name|Exception
block|{
name|integration
operator|.
name|stop
argument_list|()
expr_stmt|;
end_expr_stmt

begin_function
unit|}  	@
name|Test
specifier|public
name|void
name|testRoutes
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Adjust routes
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|routes
init|=
name|context
operator|.
name|getRouteDefinitions
argument_list|()
decl_stmt|;
name|routes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|adviceWith
argument_list|(
name|context
argument_list|,
operator|new
name|AdviceWithRouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Replace "from" endpoint with direct:start
name|replaceFromWith
argument_list|(
literal|"direct:start"
argument_list|)
expr_stmt|;
comment|// Mock and skip result endpoint
name|mockEndpointsAndSkip
argument_list|(
literal|"log:*"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|MockEndpoint
name|resultEndpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:log:foo"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// resultEndpoint.expectedMessageCount(1); // If you want to just check the number of messages
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"hello"
argument_list|)
expr_stmt|;
comment|// If you want to check the contents
comment|// You can also take the expected result from an external file
comment|// String result = IOUtils.toString(context.getClassResolver().loadResourceAsStream("testdata/out/result.txt"));
comment|// resultEndpoint.expectedBodiesReceived(result.replaceAll("\r?\n", "\n"));
comment|// Start the integration
name|integration
operator|.
name|run
argument_list|()
expr_stmt|;
comment|// Send the test message
name|context
operator|.
name|createProducerTemplate
argument_list|()
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"hello"
argument_list|)
expr_stmt|;
comment|// You can also send an external file
comment|// context.createProducerTemplate.sendBody("direct:start", context.getClassResolver().loadResourceAsStream("testdata/in/input.xml"));
comment|// REST/HTTP services can be easily tested with RestAssured:
comment|// get(context.resolvePropertyPlaceholders("{{restUrl}}")).then().statusCode(204).body(isEmptyOrNullString());
comment|// given().param("status").get(context.resolvePropertyPlaceholders("{{restUrl}}")).then().statusCode(200).body(equalTo("active"));
comment|// given().auth().basic("testuser", "testpass").body("hello").when().post(context.resolvePropertyPlaceholders("{{restUrl}}")).then().statusCode(200).body(equalTo("response"));
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
end_function

unit|}
end_unit

