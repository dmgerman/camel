begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.browse
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|browse
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|builder
operator|.
name|RouteBuilder
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
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|BrowseTest
specifier|public
class|class
name|BrowseTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|BrowseTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|body1
specifier|protected
name|Object
name|body1
init|=
literal|"one"
decl_stmt|;
DECL|field|body2
specifier|protected
name|Object
name|body2
init|=
literal|"two"
decl_stmt|;
DECL|method|testListEndpoints ()
specifier|public
name|void
name|testListEndpoints
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"browse:foo"
argument_list|,
name|body1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"browse:foo"
argument_list|,
name|body2
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Endpoint
argument_list|>
name|list
init|=
name|context
operator|.
name|getEndpoints
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"number of endpoints"
argument_list|,
literal|2
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Endpoint
name|endpoint
range|:
name|list
control|)
block|{
name|List
argument_list|<
name|Exchange
argument_list|>
name|exchanges
init|=
operator|(
operator|(
name|BrowseEndpoint
operator|)
name|endpoint
operator|)
operator|.
name|getExchanges
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|">>>> "
operator|+
name|endpoint
operator|+
literal|" has: "
operator|+
name|exchanges
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Exchanges received on "
operator|+
name|endpoint
argument_list|,
literal|2
argument_list|,
name|exchanges
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertInMessageBodyEquals
argument_list|(
name|exchanges
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|body1
argument_list|)
expr_stmt|;
name|assertInMessageBodyEquals
argument_list|(
name|exchanges
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
name|body2
argument_list|)
expr_stmt|;
block|}
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
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"browse:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"browse:bar"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

