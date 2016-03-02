begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.snakeyaml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|snakeyaml
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
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|snakeyaml
operator|.
name|model
operator|.
name|TestPojo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|yaml
operator|.
name|snakeyaml
operator|.
name|nodes
operator|.
name|Tag
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_class
DECL|class|SnakeYAMLMarshalTestHelper
specifier|public
specifier|final
class|class
name|SnakeYAMLMarshalTestHelper
block|{
DECL|method|SnakeYAMLMarshalTestHelper ()
specifier|protected
name|SnakeYAMLMarshalTestHelper
parameter_list|()
block|{     }
DECL|method|createTestPojo ()
specifier|public
specifier|static
name|TestPojo
name|createTestPojo
parameter_list|()
block|{
return|return
operator|new
name|TestPojo
argument_list|(
literal|"Camel"
argument_list|)
return|;
block|}
DECL|method|createTestMap ()
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|createTestMap
parameter_list|()
block|{
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
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
literal|"Camel"
argument_list|)
expr_stmt|;
return|return
name|map
return|;
block|}
DECL|method|createDataFormat (Class<?> type)
specifier|public
specifier|static
name|SnakeYAMLDataFormat
name|createDataFormat
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|type
operator|==
literal|null
condition|?
operator|new
name|SnakeYAMLDataFormat
argument_list|()
else|:
operator|new
name|SnakeYAMLDataFormat
argument_list|(
name|type
argument_list|)
return|;
block|}
DECL|method|createPrettyFlowDataFormat (Class<?> type, boolean prettyFlow)
specifier|public
specifier|static
name|SnakeYAMLDataFormat
name|createPrettyFlowDataFormat
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|boolean
name|prettyFlow
parameter_list|)
block|{
name|SnakeYAMLDataFormat
name|df
init|=
name|type
operator|==
literal|null
condition|?
operator|new
name|SnakeYAMLDataFormat
argument_list|()
else|:
operator|new
name|SnakeYAMLDataFormat
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|df
operator|.
name|setPrettyFlow
argument_list|(
name|prettyFlow
argument_list|)
expr_stmt|;
return|return
name|df
return|;
block|}
DECL|method|createClassTagDataFormat (Class<?> type, Tag tag)
specifier|public
specifier|static
name|SnakeYAMLDataFormat
name|createClassTagDataFormat
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Tag
name|tag
parameter_list|)
block|{
name|SnakeYAMLDataFormat
name|df
init|=
operator|new
name|SnakeYAMLDataFormat
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|df
operator|.
name|addTag
argument_list|(
name|type
argument_list|,
name|tag
argument_list|)
expr_stmt|;
return|return
name|df
return|;
block|}
DECL|method|marshalAndUnmarshal ( CamelContext context, Object body, String mockName, String directIn, String directBack, String expected)
specifier|public
specifier|static
name|void
name|marshalAndUnmarshal
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Object
name|body
parameter_list|,
name|String
name|mockName
parameter_list|,
name|String
name|directIn
parameter_list|,
name|String
name|directBack
parameter_list|,
name|String
name|expected
parameter_list|)
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|mockName
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|mock
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|body
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|equals
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|ProducerTemplate
name|template
init|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|String
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
name|directIn
argument_list|,
name|body
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|result
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
name|directBack
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

