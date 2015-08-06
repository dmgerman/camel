begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.quartz
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|quartz
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
name|CamelContext
import|;
end_import

begin_class
DECL|class|QuartzHelper
specifier|public
specifier|final
class|class
name|QuartzHelper
block|{
DECL|method|QuartzHelper ()
specifier|private
name|QuartzHelper
parameter_list|()
block|{     }
DECL|method|getQuartzContextName (CamelContext camelContext)
specifier|public
specifier|static
name|String
name|getQuartzContextName
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
comment|// favour using the actual management name which was registered in JMX (if JMX is enabled)
if|if
condition|(
name|camelContext
operator|.
name|getManagementName
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|camelContext
operator|.
name|getManagementName
argument_list|()
return|;
block|}
else|else
block|{
comment|// fallback as name
return|return
name|camelContext
operator|.
name|getName
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

