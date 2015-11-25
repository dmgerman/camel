begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.elasticsearch
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|elasticsearch
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
name|CamelContext
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|elasticsearch
operator|.
name|client
operator|.
name|Client
import|;
end_import

begin_import
import|import
name|org
operator|.
name|elasticsearch
operator|.
name|common
operator|.
name|settings
operator|.
name|ImmutableSettings
import|;
end_import

begin_import
import|import
name|org
operator|.
name|elasticsearch
operator|.
name|node
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|elasticsearch
operator|.
name|node
operator|.
name|NodeBuilder
operator|.
name|nodeBuilder
import|;
end_import

begin_class
DECL|class|ElasticsearchBaseTest
specifier|public
class|class
name|ElasticsearchBaseTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|node
specifier|private
specifier|static
name|Node
name|node
decl_stmt|;
DECL|field|client
specifier|private
specifier|static
name|Client
name|client
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|cleanupOnce ()
specifier|public
specifier|static
name|void
name|cleanupOnce
parameter_list|()
block|{
name|deleteDirectory
argument_list|(
literal|"target/data"
argument_list|)
expr_stmt|;
comment|// create an embedded node to resue
name|node
operator|=
name|nodeBuilder
argument_list|()
operator|.
name|local
argument_list|(
literal|true
argument_list|)
operator|.
name|settings
argument_list|(
name|ImmutableSettings
operator|.
name|settingsBuilder
argument_list|()
operator|.
name|put
argument_list|(
literal|"http.enabled"
argument_list|,
literal|false
argument_list|)
operator|.
name|put
argument_list|(
literal|"path.data"
argument_list|,
literal|"target/data"
argument_list|)
argument_list|)
operator|.
name|node
argument_list|()
expr_stmt|;
name|client
operator|=
name|node
operator|.
name|client
argument_list|()
expr_stmt|;
block|}
annotation|@
name|AfterClass
DECL|method|teardownOnce ()
specifier|public
specifier|static
name|void
name|teardownOnce
parameter_list|()
block|{
if|if
condition|(
name|client
operator|!=
literal|null
condition|)
block|{
name|client
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
name|node
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|isCreateCamelContextPerClass ()
specifier|public
name|boolean
name|isCreateCamelContextPerClass
parameter_list|()
block|{
comment|// let's speed up the tests using the same context
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
comment|// reuse existing client
name|ElasticsearchComponent
name|es
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"elasticsearch"
argument_list|,
name|ElasticsearchComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|es
operator|.
name|setClient
argument_list|(
name|client
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
comment|/**      * As we don't delete the {@code target/data} folder for<b>each</b> test      * below (otherwise they would run much slower), we need to make sure      * there's no side effect of the same used data through creating unique      * indexes.      */
DECL|method|createIndexedData (String... additionalPrefixes)
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|createIndexedData
parameter_list|(
name|String
modifier|...
name|additionalPrefixes
parameter_list|)
block|{
name|String
name|prefix
init|=
name|createPrefix
argument_list|()
decl_stmt|;
comment|// take over any potential prefixes we may have been asked for
if|if
condition|(
name|additionalPrefixes
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
name|prefix
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|additionalPrefix
range|:
name|additionalPrefixes
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|additionalPrefix
argument_list|)
operator|.
name|append
argument_list|(
literal|"-"
argument_list|)
expr_stmt|;
block|}
name|prefix
operator|=
name|sb
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
name|String
name|key
init|=
name|prefix
operator|+
literal|"key"
decl_stmt|;
name|String
name|value
init|=
name|prefix
operator|+
literal|"value"
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Creating indexed data using the key/value pair {} => {}"
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|map
return|;
block|}
DECL|method|createPrefix ()
name|String
name|createPrefix
parameter_list|()
block|{
comment|// make use of the test method name to avoid collision
return|return
name|getTestMethodName
argument_list|()
operator|.
name|toLowerCase
argument_list|()
operator|+
literal|"-"
return|;
block|}
block|}
end_class

end_unit

