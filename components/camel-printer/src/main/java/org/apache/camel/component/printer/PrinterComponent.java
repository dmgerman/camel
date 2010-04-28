begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.printer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|printer
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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

begin_class
DECL|class|PrinterComponent
specifier|public
class|class
name|PrinterComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|config
name|PrinterConfiguration
name|config
decl_stmt|;
DECL|method|PrinterComponent ()
specifier|public
name|PrinterComponent
parameter_list|()
block|{
name|config
operator|=
operator|new
name|PrinterConfiguration
argument_list|()
expr_stmt|;
block|}
DECL|method|PrinterComponent (CamelContext context)
specifier|public
name|PrinterComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|config
operator|=
operator|new
name|PrinterConfiguration
argument_list|()
expr_stmt|;
block|}
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
name|config
operator|.
name|parseURI
argument_list|(
operator|new
name|URI
argument_list|(
name|uri
argument_list|)
argument_list|)
expr_stmt|;
name|PrinterEndpoint
name|printerEndpoint
init|=
operator|new
name|PrinterEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|config
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|printerEndpoint
operator|.
name|getConfig
argument_list|()
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
operator|new
name|PrinterEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|config
argument_list|)
return|;
block|}
block|}
end_class

end_unit

