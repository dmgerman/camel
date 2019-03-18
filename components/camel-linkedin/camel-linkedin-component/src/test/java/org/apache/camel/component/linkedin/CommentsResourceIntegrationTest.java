begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_comment
comment|/*  * Camel Api Route test generated by camel-component-util-maven-plugin  * Generated on: Wed Jul 09 19:57:10 PDT 2014  */
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

begin_comment
comment|/**  * Test class for {@link org.apache.camel.component.linkedin.api.CommentsResource} APIs.  */
end_comment

begin_class
DECL|class|CommentsResourceIntegrationTest
specifier|public
class|class
name|CommentsResourceIntegrationTest
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
name|CommentsResourceIntegrationTest
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
literal|"comments"
decl_stmt|;
comment|// TODO provide parameter values for getComment
annotation|@
name|Ignore
annotation|@
name|Test
DECL|method|testGetComment ()
specifier|public
name|void
name|testGetComment
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
argument_list|<>
argument_list|()
decl_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.comment_id"
argument_list|,
literal|null
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
name|Comment
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://GETCOMMENT"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"getComment result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"getComment: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
comment|// TODO provide parameter values for removeComment
annotation|@
name|Ignore
annotation|@
name|Test
DECL|method|testRemoveComment ()
specifier|public
name|void
name|testRemoveComment
parameter_list|()
throws|throws
name|Exception
block|{
comment|// using String message body for single parameter "comment_id"
name|requestBody
argument_list|(
literal|"direct://REMOVECOMMENT"
argument_list|,
literal|null
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
comment|// test route for getComment
name|from
argument_list|(
literal|"direct://GETCOMMENT"
argument_list|)
operator|.
name|to
argument_list|(
literal|"linkedin://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/getComment"
argument_list|)
expr_stmt|;
comment|// test route for removeComment
name|from
argument_list|(
literal|"direct://REMOVECOMMENT"
argument_list|)
operator|.
name|to
argument_list|(
literal|"linkedin://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/removeComment?inBody=comment_id"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

