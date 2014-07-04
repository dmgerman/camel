begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Camel Api Route test generated by camel-component-util-maven-plugin  * Generated on: Thu Jul 03 16:04:18 NDT 2014  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.drive
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|google
operator|.
name|drive
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
name|google
operator|.
name|drive
operator|.
name|internal
operator|.
name|GoogleDriveApiCollection
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
name|google
operator|.
name|drive
operator|.
name|internal
operator|.
name|DriveChangesApiMethod
import|;
end_import

begin_comment
comment|/**  * Test class for com.google.api.services.drive.Drive$Changes APIs.  * TODO Move the file to src/test/java, populate parameter values, and remove @Ignore annotations.  * The class source won't be generated again if the generator MOJO finds it under src/test/java.  */
end_comment

begin_class
DECL|class|DriveChangesIntegrationTest
specifier|public
class|class
name|DriveChangesIntegrationTest
extends|extends
name|AbstractGoogleDriveTestSupport
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
name|DriveChangesIntegrationTest
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
name|GoogleDriveApiCollection
operator|.
name|getCollection
argument_list|()
operator|.
name|getApiName
argument_list|(
name|DriveChangesApiMethod
operator|.
name|class
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|// TODO provide parameter values for get
annotation|@
name|Ignore
annotation|@
name|Test
DECL|method|testGet ()
specifier|public
name|void
name|testGet
parameter_list|()
throws|throws
name|Exception
block|{
comment|// using String message body for single parameter "changeId"
specifier|final
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|drive
operator|.
name|Drive
operator|.
name|Changes
operator|.
name|Get
name|result
init|=
name|requestBody
argument_list|(
literal|"direct://GET"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"get result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"get: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Ignore
annotation|@
name|Test
DECL|method|testList ()
specifier|public
name|void
name|testList
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|drive
operator|.
name|Drive
operator|.
name|Changes
operator|.
name|List
name|result
init|=
name|requestBody
argument_list|(
literal|"direct://LIST"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"list result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"list: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
comment|// TODO provide parameter values for watch
annotation|@
name|Ignore
annotation|@
name|Test
DECL|method|testWatch ()
specifier|public
name|void
name|testWatch
parameter_list|()
throws|throws
name|Exception
block|{
comment|// using com.google.api.services.drive.model.Channel message body for single parameter "contentChannel"
specifier|final
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|drive
operator|.
name|Drive
operator|.
name|Changes
operator|.
name|Watch
name|result
init|=
name|requestBody
argument_list|(
literal|"direct://WATCH"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"watch result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"watch: "
operator|+
name|result
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
comment|// test route for get
name|from
argument_list|(
literal|"direct://GET"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-drive://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/get?inBody=changeId"
argument_list|)
expr_stmt|;
comment|// test route for list
name|from
argument_list|(
literal|"direct://LIST"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-drive://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/list"
argument_list|)
expr_stmt|;
comment|// test route for watch
name|from
argument_list|(
literal|"direct://WATCH"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-drive://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/watch?inBody=contentChannel"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

