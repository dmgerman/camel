begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.micrometer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|micrometer
package|;
end_package

begin_import
import|import
name|io
operator|.
name|micrometer
operator|.
name|jmx
operator|.
name|JmxConfig
import|;
end_import

begin_comment
comment|/**  * JmxConfig with a custom domain name  */
end_comment

begin_class
DECL|class|CamelJmxConfig
specifier|public
specifier|final
class|class
name|CamelJmxConfig
implements|implements
name|JmxConfig
block|{
DECL|field|DEFAULT
specifier|public
specifier|static
specifier|final
name|CamelJmxConfig
name|DEFAULT
init|=
operator|new
name|CamelJmxConfig
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|get (String key)
specifier|public
name|String
name|get
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|prefix ()
specifier|public
name|String
name|prefix
parameter_list|()
block|{
return|return
literal|"jmx"
return|;
block|}
annotation|@
name|Override
DECL|method|domain ()
specifier|public
name|String
name|domain
parameter_list|()
block|{
return|return
literal|"org.apache.camel.micrometer"
return|;
block|}
block|}
end_class

end_unit

