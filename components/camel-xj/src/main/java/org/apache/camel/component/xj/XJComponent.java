begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xj
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xj
package|;
end_package

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
name|xslt
operator|.
name|XsltComponent
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
name|xslt
operator|.
name|XsltEndpoint
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
comment|/**  * The<a href="https://camel.apache.org/components/latest/xj-component.html">XJ Component</a> is for performing xml to json and back transformations of messages  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"xj"
argument_list|)
DECL|class|XJComponent
specifier|public
class|class
name|XJComponent
extends|extends
name|XsltComponent
block|{
DECL|method|XJComponent ()
specifier|public
name|XJComponent
parameter_list|()
block|{     }
DECL|method|createXsltEndpoint (String uri)
specifier|protected
name|XsltEndpoint
name|createXsltEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
operator|new
name|XJEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
return|;
block|}
block|}
end_class

end_unit

