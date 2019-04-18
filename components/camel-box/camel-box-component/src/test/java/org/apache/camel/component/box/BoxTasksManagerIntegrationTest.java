begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.box
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|box
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|List
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
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxAPIConnection
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxFolder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxTask
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxTask
operator|.
name|Action
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxTaskAssignment
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxUser
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
name|box
operator|.
name|api
operator|.
name|BoxTasksManager
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
name|box
operator|.
name|internal
operator|.
name|BoxApiCollection
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
name|box
operator|.
name|internal
operator|.
name|BoxTasksManagerApiMethod
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

begin_comment
comment|/**  * Test class for {@link BoxTasksManager}  * APIs.  */
end_comment

begin_class
DECL|class|BoxTasksManagerIntegrationTest
specifier|public
class|class
name|BoxTasksManagerIntegrationTest
extends|extends
name|AbstractBoxTestSupport
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
name|BoxTasksManagerIntegrationTest
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
name|BoxApiCollection
operator|.
name|getCollection
argument_list|()
operator|.
name|getApiName
argument_list|(
name|BoxTasksManagerApiMethod
operator|.
name|class
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
DECL|field|CAMEL_TEST_FILE
specifier|private
specifier|static
specifier|final
name|String
name|CAMEL_TEST_FILE
init|=
literal|"/CamelTestFile.txt"
decl_stmt|;
DECL|field|CAMEL_TEST_FILE_NAME
specifier|private
specifier|static
specifier|final
name|String
name|CAMEL_TEST_FILE_NAME
init|=
literal|"CamelTestFile.txt"
decl_stmt|;
DECL|field|CAMEL_TEST_MESSAGE
specifier|private
specifier|static
specifier|final
name|String
name|CAMEL_TEST_MESSAGE
init|=
literal|"Camel Test Message"
decl_stmt|;
DECL|field|TEN_MINUTES_IN_MILLIS
specifier|private
specifier|static
specifier|final
name|long
name|TEN_MINUTES_IN_MILLIS
init|=
literal|600000
decl_stmt|;
DECL|field|testTask
specifier|private
name|BoxTask
name|testTask
decl_stmt|;
annotation|@
name|Ignore
comment|//needs https://community.box.com/t5/custom/page/page-id/BoxViewTicketDetail?ticket_id=1895413 to be solved
annotation|@
name|Test
DECL|method|testAddAssignmentToTask ()
specifier|public
name|void
name|testAddAssignmentToTask
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxTask
name|result
init|=
literal|null
decl_stmt|;
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
argument_list|<>
argument_list|()
decl_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.taskId"
argument_list|,
name|testTask
operator|.
name|getID
argument_list|()
argument_list|)
expr_stmt|;
comment|// parameter type is com.box.sdk.BoxUser
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.assignTo"
argument_list|,
name|getCurrentUser
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://ADDASSIGNMENTTOTASK"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"addAssignmentToTask result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"addAssignmentToTask: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAddFileTask ()
specifier|public
name|void
name|testAddFileTask
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxTask
name|result
init|=
literal|null
decl_stmt|;
try|try
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
argument_list|<>
argument_list|()
decl_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.fileId"
argument_list|,
name|testFile
operator|.
name|getID
argument_list|()
argument_list|)
expr_stmt|;
comment|// parameter type is com.box.sdk.BoxTask.Action
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.action"
argument_list|,
name|BoxTask
operator|.
name|Action
operator|.
name|REVIEW
argument_list|)
expr_stmt|;
comment|// parameter type is java.util.Date
name|Date
name|now
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|Date
name|dueAt
init|=
operator|new
name|Date
argument_list|(
name|now
operator|.
name|getTime
argument_list|()
operator|+
name|TEN_MINUTES_IN_MILLIS
argument_list|)
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.dueAt"
argument_list|,
name|dueAt
argument_list|)
expr_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.message"
argument_list|,
name|CAMEL_TEST_MESSAGE
argument_list|)
expr_stmt|;
name|result
operator|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://ADDFILETASK"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"addFileTask result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"addFileTask: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|result
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{                 }
block|}
block|}
block|}
annotation|@
name|Test
DECL|method|testDeleteTask ()
specifier|public
name|void
name|testDeleteTask
parameter_list|()
throws|throws
name|Exception
block|{
comment|// using String message body for single parameter "taskId"
name|requestBody
argument_list|(
literal|"direct://DELETETASK"
argument_list|,
name|testTask
operator|.
name|getID
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|BoxTask
operator|.
name|Info
argument_list|>
name|tasks
init|=
name|testFile
operator|.
name|getTasks
argument_list|()
decl_stmt|;
name|boolean
name|exists
init|=
name|tasks
operator|.
name|size
argument_list|()
operator|!=
literal|0
decl_stmt|;
name|assertEquals
argument_list|(
literal|"deleteTask task still exists."
argument_list|,
literal|false
argument_list|,
name|exists
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Ignore
comment|// Receiving "not found" exception from Box API
annotation|@
name|Test
DECL|method|testDeleteTaskAssignment ()
specifier|public
name|void
name|testDeleteTaskAssignment
parameter_list|()
throws|throws
name|Exception
block|{
name|BoxTaskAssignment
operator|.
name|Info
name|info
init|=
name|testTask
operator|.
name|addAssignment
argument_list|(
name|getCurrentUser
argument_list|()
argument_list|)
decl_stmt|;
comment|// using String message body for single parameter "taskAssignmentId"
name|requestBody
argument_list|(
literal|"direct://DELETETASKASSIGNMENT"
argument_list|,
name|info
operator|.
name|getID
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|BoxTaskAssignment
operator|.
name|Info
argument_list|>
name|assignments
init|=
name|testTask
operator|.
name|getAssignments
argument_list|()
decl_stmt|;
name|boolean
name|exists
init|=
name|assignments
operator|.
name|size
argument_list|()
operator|!=
literal|0
decl_stmt|;
name|assertEquals
argument_list|(
literal|"deleteTaskAssignment assignment still exists."
argument_list|,
literal|false
argument_list|,
name|exists
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetFileTasks ()
specifier|public
name|void
name|testGetFileTasks
parameter_list|()
throws|throws
name|Exception
block|{
comment|// using String message body for single parameter "fileId"
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|final
name|java
operator|.
name|util
operator|.
name|List
name|result
init|=
name|requestBody
argument_list|(
literal|"direct://GETFILETASKS"
argument_list|,
name|testFile
operator|.
name|getID
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"getFileTasks result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"getFileTasks: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Ignore
annotation|@
name|Test
DECL|method|testGetTaskAssignmentInfo ()
specifier|public
name|void
name|testGetTaskAssignmentInfo
parameter_list|()
throws|throws
name|Exception
block|{
name|BoxTaskAssignment
operator|.
name|Info
name|info
init|=
name|testTask
operator|.
name|addAssignment
argument_list|(
name|getCurrentUser
argument_list|()
argument_list|)
decl_stmt|;
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxTaskAssignment
operator|.
name|Info
name|result
init|=
literal|null
decl_stmt|;
try|try
block|{
comment|// using String message body for single parameter "taskAssignmentId"
name|result
operator|=
name|requestBody
argument_list|(
literal|"direct://GETTASKASSIGNMENTINFO"
argument_list|,
name|info
operator|.
name|getID
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"getTaskAssignmentInfo result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"getTaskAssignmentInfo: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
try|try
block|{
operator|(
operator|(
name|BoxTaskAssignment
operator|)
name|result
operator|.
name|getResource
argument_list|()
operator|)
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{                 }
block|}
block|}
block|}
annotation|@
name|Ignore
comment|//needs https://community.box.com/t5/custom/page/page-id/BoxViewTicketDetail?ticket_id=1895413 to be solved
annotation|@
name|Test
DECL|method|testGetTaskAssignments ()
specifier|public
name|void
name|testGetTaskAssignments
parameter_list|()
throws|throws
name|Exception
block|{
comment|// using String message body for single parameter "taskId"
comment|//add assignment to task -> to be able to search for assignments
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
argument_list|<>
argument_list|()
decl_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.taskId"
argument_list|,
name|testTask
operator|.
name|getID
argument_list|()
argument_list|)
expr_stmt|;
comment|// parameter type is com.box.sdk.BoxUser
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.assignTo"
argument_list|,
name|getCurrentUser
argument_list|()
argument_list|)
expr_stmt|;
name|requestBodyAndHeaders
argument_list|(
literal|"direct://ADDASSIGNMENTTOTASK"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|final
name|java
operator|.
name|util
operator|.
name|List
name|result
init|=
name|requestBody
argument_list|(
literal|"direct://GETTASKASSIGNMENTS"
argument_list|,
name|testTask
operator|.
name|getID
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"getTaskAssignments result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"getTaskAssignments: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetTaskInfo ()
specifier|public
name|void
name|testGetTaskInfo
parameter_list|()
throws|throws
name|Exception
block|{
comment|// using String message body for single parameter "taskId"
specifier|final
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxTask
operator|.
name|Info
name|result
init|=
name|requestBody
argument_list|(
literal|"direct://GETTASKINFO"
argument_list|,
name|testTask
operator|.
name|getID
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"getTaskInfo result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"getTaskInfo: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Ignore
comment|// No way to change BoxTask.Info parameters
annotation|@
name|Test
DECL|method|testUpdateTaskInfo ()
specifier|public
name|void
name|testUpdateTaskInfo
parameter_list|()
throws|throws
name|Exception
block|{
name|BoxTask
operator|.
name|Info
name|info
init|=
name|testTask
operator|.
name|getInfo
argument_list|()
decl_stmt|;
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
argument_list|<>
argument_list|()
decl_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.taskId"
argument_list|,
name|testTask
operator|.
name|getID
argument_list|()
argument_list|)
expr_stmt|;
comment|// parameter type is com.box.sdk.BoxTask.Info
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.info"
argument_list|,
name|info
argument_list|)
expr_stmt|;
specifier|final
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxTask
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://UPDATETASKINFO"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"updateTaskInfo result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"updateTaskInfo: "
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
comment|// test route for addAssignmentToTask
name|from
argument_list|(
literal|"direct://ADDASSIGNMENTTOTASK"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/addAssignmentToTask"
argument_list|)
expr_stmt|;
comment|// test route for addFileTask
name|from
argument_list|(
literal|"direct://ADDFILETASK"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/addFileTask"
argument_list|)
expr_stmt|;
comment|// test route for deleteTask
name|from
argument_list|(
literal|"direct://DELETETASK"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/deleteTask?inBody=taskId"
argument_list|)
expr_stmt|;
comment|// test route for deleteTaskAssignment
name|from
argument_list|(
literal|"direct://DELETETASKASSIGNMENT"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/deleteTaskAssignment?inBody=taskAssignmentId"
argument_list|)
expr_stmt|;
comment|// test route for getFileTasks
name|from
argument_list|(
literal|"direct://GETFILETASKS"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/getFileTasks?inBody=fileId"
argument_list|)
expr_stmt|;
comment|// test route for getTaskAssignmentInfo
name|from
argument_list|(
literal|"direct://GETTASKASSIGNMENTINFO"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/getTaskAssignmentInfo?inBody=taskAssignmentId"
argument_list|)
expr_stmt|;
comment|// test route for getTaskAssignments
name|from
argument_list|(
literal|"direct://GETTASKASSIGNMENTS"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/getTaskAssignments?inBody=taskId"
argument_list|)
expr_stmt|;
comment|// test route for getTaskInfo
name|from
argument_list|(
literal|"direct://GETTASKINFO"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/getTaskInfo?inBody=taskId"
argument_list|)
expr_stmt|;
comment|// test route for updateTaskInfo
name|from
argument_list|(
literal|"direct://UPDATETASKINFO"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/updateTaskInfo"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Before
DECL|method|setupTest ()
specifier|public
name|void
name|setupTest
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestFile
argument_list|()
expr_stmt|;
name|createTestTask
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|teardownTest ()
specifier|public
name|void
name|teardownTest
parameter_list|()
block|{
name|deleteTestTask
argument_list|()
expr_stmt|;
name|deleteTestFile
argument_list|()
expr_stmt|;
block|}
DECL|method|getConnection ()
specifier|public
name|BoxAPIConnection
name|getConnection
parameter_list|()
block|{
name|BoxEndpoint
name|endpoint
init|=
operator|(
name|BoxEndpoint
operator|)
name|context
argument_list|()
operator|.
name|getEndpoint
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/addAssignmentToTask"
argument_list|)
decl_stmt|;
return|return
name|endpoint
operator|.
name|getBoxConnection
argument_list|()
return|;
block|}
DECL|method|createTestTask ()
specifier|private
name|void
name|createTestTask
parameter_list|()
block|{
name|Date
name|now
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|Date
name|dueAt
init|=
operator|new
name|Date
argument_list|(
name|now
operator|.
name|getTime
argument_list|()
operator|+
name|TEN_MINUTES_IN_MILLIS
argument_list|)
decl_stmt|;
name|testTask
operator|=
name|testFile
operator|.
name|addTask
argument_list|(
name|Action
operator|.
name|REVIEW
argument_list|,
name|CAMEL_TEST_MESSAGE
argument_list|,
name|dueAt
argument_list|)
operator|.
name|getResource
argument_list|()
expr_stmt|;
block|}
DECL|method|deleteTestTask ()
specifier|private
name|void
name|deleteTestTask
parameter_list|()
block|{
try|try
block|{
name|testTask
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{         }
name|testTask
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|createTestFile ()
specifier|private
name|void
name|createTestFile
parameter_list|()
throws|throws
name|FileNotFoundException
block|{
name|BoxFolder
name|rootFolder
init|=
name|BoxFolder
operator|.
name|getRootFolder
argument_list|(
name|getConnection
argument_list|()
argument_list|)
decl_stmt|;
name|InputStream
name|stream
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|CAMEL_TEST_FILE
argument_list|)
decl_stmt|;
name|testFile
operator|=
name|rootFolder
operator|.
name|uploadFile
argument_list|(
name|stream
argument_list|,
name|CAMEL_TEST_FILE_NAME
argument_list|)
operator|.
name|getResource
argument_list|()
expr_stmt|;
block|}
DECL|method|getCurrentUser ()
specifier|private
name|BoxUser
name|getCurrentUser
parameter_list|()
block|{
return|return
name|BoxUser
operator|.
name|getCurrentUser
argument_list|(
name|getConnection
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

