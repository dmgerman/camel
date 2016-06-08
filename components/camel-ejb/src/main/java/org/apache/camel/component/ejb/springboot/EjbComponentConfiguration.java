begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ejb.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ejb
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * The ejb component is for invoking EJB Java beans from Camel.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.ejb"
argument_list|)
DECL|class|EjbComponentConfiguration
specifier|public
class|class
name|EjbComponentConfiguration
block|{
comment|/**      * The Context to use for looking up the EJBs      */
DECL|field|context
specifier|private
name|Context
name|context
decl_stmt|;
comment|/**      * Properties for creating javax.naming.Context if a context has not been      * configured.      */
DECL|field|properties
specifier|private
name|Properties
name|properties
decl_stmt|;
DECL|method|getContext ()
specifier|public
name|Context
name|getContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
DECL|method|setContext (Context context)
specifier|public
name|void
name|setContext
parameter_list|(
name|Context
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
DECL|method|getProperties ()
specifier|public
name|Properties
name|getProperties
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
DECL|method|setProperties (Properties properties)
specifier|public
name|void
name|setProperties
parameter_list|(
name|Properties
name|properties
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
block|}
end_class

end_unit

