begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.validator.msv
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|validator
operator|.
name|msv
package|;
end_package

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
name|javax
operator|.
name|xml
operator|.
name|validation
operator|.
name|SchemaFactory
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
name|component
operator|.
name|validator
operator|.
name|ValidatorComponent
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
name|spi
operator|.
name|Metadata
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
name|spi
operator|.
name|annotations
operator|.
name|Component
import|;
end_import

begin_comment
comment|/**  * The<a href="http://camel.apache.org/msv.html">MSV Component</a> uses the  *<a href="https://msv.dev.java.net/">MSV library</a> to perform XML validation using pluggable XML schema languages.  *  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"msv"
argument_list|)
DECL|class|MsvComponent
specifier|public
class|class
name|MsvComponent
extends|extends
name|ValidatorComponent
block|{
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|schemaFactory
specifier|private
name|SchemaFactory
name|schemaFactory
decl_stmt|;
DECL|method|MsvComponent ()
specifier|public
name|MsvComponent
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|MsvEndpoint
name|endpoint
init|=
operator|new
name|MsvEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|remaining
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setSchemaFactory
argument_list|(
name|getSchemaFactory
argument_list|()
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
DECL|method|getSchemaFactory ()
specifier|public
name|SchemaFactory
name|getSchemaFactory
parameter_list|()
block|{
return|return
name|schemaFactory
return|;
block|}
comment|/**      * To use the {@link javax.xml.validation.SchemaFactory}.      */
DECL|method|setSchemaFactory (SchemaFactory schemaFactory)
specifier|public
name|void
name|setSchemaFactory
parameter_list|(
name|SchemaFactory
name|schemaFactory
parameter_list|)
block|{
name|this
operator|.
name|schemaFactory
operator|=
name|schemaFactory
expr_stmt|;
block|}
block|}
end_class

end_unit

