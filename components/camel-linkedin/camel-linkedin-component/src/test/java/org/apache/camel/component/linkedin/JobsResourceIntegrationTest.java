begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Camel Api Route test generated by camel-component-util-maven-plugin  * Generated on: Wed Jul 09 19:57:11 PDT 2014  */
end_comment

begin_package
DECL|package|org.apache.camel.component.linkedin
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|linkedin
package|;
end_package

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
name|junit
operator|.
name|Ignore
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
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|linkedin
operator|.
name|internal
operator|.
name|LinkedInApiCollection
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
name|linkedin
operator|.
name|internal
operator|.
name|JobsResourceApiMethod
import|;
end_import

begin_comment
comment|/**  * Test class for {@link org.apache.camel.component.linkedin.api.JobsResource} APIs.  */
end_comment

begin_class
DECL|class|JobsResourceIntegrationTest
specifier|public
class|class
name|JobsResourceIntegrationTest
extends|extends
name|AbstractLinkedInTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|JobsResourceIntegrationTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|PATH_PREFIX
specifier|private
specifier|static
specifier|final
name|String
name|PATH_PREFIX
init|=
name|LinkedInApiCollection
operator|.
name|getCollection
argument_list|()
operator|.
name|getApiName
argument_list|(
name|JobsResourceApiMethod
operator|.
name|class
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|// TODO provide parameter values for addJob
annotation|@
name|Ignore
annotation|@
name|Test
DECL|method|testAddJob ()
specifier|public
name|void
name|testAddJob
parameter_list|()
throws|throws
name|Exception
block|{
comment|// using org.apache.camel.component.linkedin.api.model.Job message body for single parameter "job"
name|requestBody
argument_list|(
literal|"direct://ADDJOB"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|// TODO provide parameter values for editJob
annotation|@
name|Ignore
annotation|@
name|Test
DECL|method|testEditJob ()
specifier|public
name|void
name|testEditJob
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.partner_job_id"
argument_list|,
literal|0L
argument_list|)
expr_stmt|;
comment|// parameter type is org.apache.camel.component.linkedin.api.model.Job
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.job"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|requestBodyAndHeaders
argument_list|(
literal|"direct://EDITJOB"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
expr_stmt|;
block|}
comment|// TODO provide parameter values for getJob
annotation|@
name|Ignore
annotation|@
name|Test
DECL|method|testGetJob ()
specifier|public
name|void
name|testGetJob
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.job_id"
argument_list|,
literal|0L
argument_list|)
expr_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.fields"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
specifier|final
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|linkedin
operator|.
name|api
operator|.
name|model
operator|.
name|Job
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://GETJOB"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"getJob result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"getJob: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
comment|// TODO provide parameter values for removeJob
annotation|@
name|Ignore
annotation|@
name|Test
DECL|method|testRemoveJob ()
specifier|public
name|void
name|testRemoveJob
parameter_list|()
throws|throws
name|Exception
block|{
comment|// using long message body for single parameter "partner_job_id"
name|requestBody
argument_list|(
literal|"direct://REMOVEJOB"
argument_list|,
literal|0L
argument_list|)
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// test route for addJob
name|from
argument_list|(
literal|"direct://ADDJOB"
argument_list|)
operator|.
name|to
argument_list|(
literal|"linkedin://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/addJob?inBody=job"
argument_list|)
expr_stmt|;
comment|// test route for editJob
name|from
argument_list|(
literal|"direct://EDITJOB"
argument_list|)
operator|.
name|to
argument_list|(
literal|"linkedin://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/editJob"
argument_list|)
expr_stmt|;
comment|// test route for getJob
name|from
argument_list|(
literal|"direct://GETJOB"
argument_list|)
operator|.
name|to
argument_list|(
literal|"linkedin://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/getJob"
argument_list|)
expr_stmt|;
comment|// test route for removeJob
name|from
argument_list|(
literal|"direct://REMOVEJOB"
argument_list|)
operator|.
name|to
argument_list|(
literal|"linkedin://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/removeJob?inBody=partner_job_id"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

