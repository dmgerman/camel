begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dataformat
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|dataformat
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
name|impl
operator|.
name|DefaultComponent
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
name|DataFormat
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
name|util
operator|.
name|EndpointHelper
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_class
DECL|class|DataFormatComponent
specifier|public
class|class
name|DataFormatComponent
extends|extends
name|DefaultComponent
block|{
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
name|String
name|name
init|=
name|ObjectHelper
operator|.
name|before
argument_list|(
name|remaining
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
name|DataFormat
name|df
init|=
name|getCamelContext
argument_list|()
operator|.
name|resolveDataFormat
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|df
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot find data format with name: "
operator|+
name|name
argument_list|)
throw|;
block|}
name|String
name|operation
init|=
name|ObjectHelper
operator|.
name|after
argument_list|(
name|remaining
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
literal|"marshal"
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
operator|&&
operator|!
literal|"unmarshal"
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Operation must be either marshal or unmarshal, was: "
operator|+
name|operation
argument_list|)
throw|;
block|}
comment|// set reference properties first as they use # syntax that fools the regular properties setter
name|EndpointHelper
operator|.
name|setReferenceProperties
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|df
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|EndpointHelper
operator|.
name|setProperties
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|df
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|DataFormatEndpoint
name|endpoint
init|=
operator|new
name|DataFormatEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|df
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setOperation
argument_list|(
name|operation
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
block|}
end_class

end_unit

