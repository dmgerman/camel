begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|camel
operator|.
name|impl
operator|.
name|ThrottlingExceptionRoutePolicy
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

begin_class
DECL|class|ThrottlingExceptionRoutePolicyKeepOpenOnInitTest
specifier|public
class|class
name|ThrottlingExceptionRoutePolicyKeepOpenOnInitTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|url
specifier|private
name|String
name|url
init|=
literal|"seda:foo?concurrentConsumers=20"
decl_stmt|;
DECL|field|result
specifier|private
name|MockEndpoint
name|result
decl_stmt|;
DECL|field|size
specifier|private
name|int
name|size
init|=
literal|5
decl_stmt|;
DECL|field|policy
specifier|private
name|ThrottlingExceptionRoutePolicy
name|policy
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|createPolicy
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|this
operator|.
name|setUseRouteBuilder
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|result
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|context
operator|.
name|getShutdownStrategy
argument_list|()
operator|.
name|setTimeout
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
DECL|method|createPolicy ()
specifier|protected
name|void
name|createPolicy
parameter_list|()
block|{
name|int
name|threshold
init|=
literal|2
decl_stmt|;
name|long
name|failureWindow
init|=
literal|30
decl_stmt|;
name|long
name|halfOpenAfter
init|=
literal|1000
decl_stmt|;
name|boolean
name|keepOpen
init|=
literal|true
decl_stmt|;
name|policy
operator|=
operator|new
name|ThrottlingExceptionRoutePolicy
argument_list|(
name|threshold
argument_list|,
name|failureWindow
argument_list|,
name|halfOpenAfter
argument_list|,
literal|null
argument_list|,
name|keepOpen
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testThrottlingRoutePolicyStartWithAlwaysOpenOn ()
specifier|public
name|void
name|testThrottlingRoutePolicyStartWithAlwaysOpenOn
parameter_list|()
throws|throws
name|Exception
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"---- sending some messages"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
name|url
argument_list|,
literal|"Message "
operator|+
name|i
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|3
argument_list|)
expr_stmt|;
block|}
comment|// gives time for policy half open check to run every second
comment|// and should not close b/c keepOpen is true
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
comment|// gives time for policy half open check to run every second
comment|// but it should never close b/c keepOpen is true
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|result
operator|.
name|setResultWaitTime
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testThrottlingRoutePolicyStartWithAlwaysOpenOnThenClose ()
specifier|public
name|void
name|testThrottlingRoutePolicyStartWithAlwaysOpenOnThenClose
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
name|url
argument_list|,
literal|"Message "
operator|+
name|i
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|3
argument_list|)
expr_stmt|;
block|}
comment|// gives time for policy half open check to run every second
comment|// and should not close b/c keepOpen is true
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|result
operator|.
name|setResultWaitTime
argument_list|(
literal|1500
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// set keepOpen to false
comment|// now half open check will succeed
name|policy
operator|.
name|setKeepOpen
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// gives time for policy half open check to run every second
comment|// and should close and get all the messages
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|result
operator|.
name|setResultWaitTime
argument_list|(
literal|1500
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
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
name|from
argument_list|(
name|url
argument_list|)
operator|.
name|routePolicy
argument_list|(
name|policy
argument_list|)
operator|.
name|log
argument_list|(
literal|"${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:foo?groupSize=10"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

