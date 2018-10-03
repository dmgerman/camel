begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.pdf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pdf
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
name|support
operator|.
name|DefaultComponent
import|;
end_import

begin_class
DECL|class|PdfComponent
specifier|public
class|class
name|PdfComponent
extends|extends
name|DefaultComponent
block|{
DECL|method|PdfComponent ()
specifier|public
name|PdfComponent
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
name|PdfConfiguration
name|pdfConfiguration
init|=
operator|new
name|PdfConfiguration
argument_list|()
decl_stmt|;
name|setProperties
argument_list|(
name|pdfConfiguration
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|pdfConfiguration
operator|.
name|setOperation
argument_list|(
operator|new
name|URI
argument_list|(
name|uri
argument_list|)
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|new
name|PdfEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|pdfConfiguration
argument_list|)
return|;
block|}
block|}
end_class

end_unit

