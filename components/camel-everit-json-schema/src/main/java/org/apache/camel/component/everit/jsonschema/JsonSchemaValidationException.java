begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.everit.jsonschema
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|everit
operator|.
name|jsonschema
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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
name|ValidationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|everit
operator|.
name|json
operator|.
name|schema
operator|.
name|Schema
import|;
end_import

begin_class
DECL|class|JsonSchemaValidationException
specifier|public
class|class
name|JsonSchemaValidationException
extends|extends
name|ValidationException
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|method|JsonSchemaValidationException (Exchange exchange, Schema schema, org.everit.json.schema.ValidationException e)
specifier|public
name|JsonSchemaValidationException
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Schema
name|schema
parameter_list|,
name|org
operator|.
name|everit
operator|.
name|json
operator|.
name|schema
operator|.
name|ValidationException
name|e
parameter_list|)
block|{
name|super
argument_list|(
name|e
operator|.
name|getAllMessages
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|joining
argument_list|(
literal|", "
argument_list|)
argument_list|)
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
DECL|method|JsonSchemaValidationException (Exchange exchange, Schema schema, Exception e)
specifier|public
name|JsonSchemaValidationException
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Schema
name|schema
parameter_list|,
name|Exception
name|e
parameter_list|)
block|{
name|super
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

