begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jsonvalidator
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jsonvalidator
package|;
end_package

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
name|com
operator|.
name|networknt
operator|.
name|schema
operator|.
name|JsonSchema
import|;
end_import

begin_import
import|import
name|com
operator|.
name|networknt
operator|.
name|schema
operator|.
name|JsonSchemaFactory
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

begin_class
DECL|class|DefaultJsonSchemaLoader
specifier|public
class|class
name|DefaultJsonSchemaLoader
implements|implements
name|JsonSchemaLoader
block|{
annotation|@
name|Override
DECL|method|createSchema (CamelContext camelContext, InputStream inputStream)
specifier|public
name|JsonSchema
name|createSchema
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|InputStream
name|inputStream
parameter_list|)
throws|throws
name|Exception
block|{
name|JsonSchemaFactory
name|factory
init|=
name|JsonSchemaFactory
operator|.
name|getInstance
argument_list|()
decl_stmt|;
return|return
name|factory
operator|.
name|getSchema
argument_list|(
name|inputStream
argument_list|)
return|;
block|}
block|}
end_class

end_unit

