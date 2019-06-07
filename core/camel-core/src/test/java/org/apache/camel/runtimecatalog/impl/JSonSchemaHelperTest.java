begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.runtimecatalog.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|runtimecatalog
operator|.
name|impl
package|;
end_package

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
name|runtimecatalog
operator|.
name|RuntimeCamelCatalog
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
name|support
operator|.
name|JSonSchemaHelper
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
DECL|class|JSonSchemaHelperTest
specifier|public
class|class
name|JSonSchemaHelperTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testParseJsonSchemaMustBeOrdered ()
specifier|public
name|void
name|testParseJsonSchemaMustBeOrdered
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|json
init|=
name|context
operator|.
name|getExtension
argument_list|(
name|RuntimeCamelCatalog
operator|.
name|class
argument_list|)
operator|.
name|componentJSonSchema
argument_list|(
literal|"bean"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|json
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|rows
init|=
name|JSonSchemaHelper
operator|.
name|parseJsonSchema
argument_list|(
literal|"component"
argument_list|,
name|json
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|17
argument_list|,
name|rows
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// should preserve order
name|assertEquals
argument_list|(
literal|"kind"
argument_list|,
name|rows
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"scheme"
argument_list|,
name|rows
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"syntax"
argument_list|,
name|rows
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"title"
argument_list|,
name|rows
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"description"
argument_list|,
name|rows
operator|.
name|get
argument_list|(
literal|4
argument_list|)
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"label"
argument_list|,
name|rows
operator|.
name|get
argument_list|(
literal|5
argument_list|)
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"deprecated"
argument_list|,
name|rows
operator|.
name|get
argument_list|(
literal|6
argument_list|)
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"deprecationNote"
argument_list|,
name|rows
operator|.
name|get
argument_list|(
literal|7
argument_list|)
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"async"
argument_list|,
name|rows
operator|.
name|get
argument_list|(
literal|8
argument_list|)
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"consumerOnly"
argument_list|,
name|rows
operator|.
name|get
argument_list|(
literal|9
argument_list|)
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"producerOnly"
argument_list|,
name|rows
operator|.
name|get
argument_list|(
literal|10
argument_list|)
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"lenientProperties"
argument_list|,
name|rows
operator|.
name|get
argument_list|(
literal|11
argument_list|)
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"javaType"
argument_list|,
name|rows
operator|.
name|get
argument_list|(
literal|12
argument_list|)
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"firstVersion"
argument_list|,
name|rows
operator|.
name|get
argument_list|(
literal|13
argument_list|)
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"groupId"
argument_list|,
name|rows
operator|.
name|get
argument_list|(
literal|14
argument_list|)
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artifactId"
argument_list|,
name|rows
operator|.
name|get
argument_list|(
literal|15
argument_list|)
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"version"
argument_list|,
name|rows
operator|.
name|get
argument_list|(
literal|16
argument_list|)
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|rows
operator|=
name|JSonSchemaHelper
operator|.
name|parseJsonSchema
argument_list|(
literal|"properties"
argument_list|,
name|json
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|7
argument_list|,
name|rows
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"beanName"
argument_list|,
name|rows
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cache"
argument_list|,
name|rows
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"method"
argument_list|,
name|rows
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"parameters"
argument_list|,
name|rows
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"lazyStartProducer"
argument_list|,
name|rows
operator|.
name|get
argument_list|(
literal|4
argument_list|)
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|rows
operator|.
name|get
argument_list|(
literal|5
argument_list|)
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"synchronous"
argument_list|,
name|rows
operator|.
name|get
argument_list|(
literal|6
argument_list|)
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testParseInvalidJson ()
specifier|public
name|void
name|testParseInvalidJson
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|json
init|=
literal|"{ \"name\": this is invalid }"
decl_stmt|;
try|try
block|{
name|JSonSchemaHelper
operator|.
name|parseJsonSchema
argument_list|(
literal|"foo"
argument_list|,
name|json
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should fail"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Cannot parse json"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

