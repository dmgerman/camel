begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.osgi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|osgi
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
name|impl
operator|.
name|DefaultCamelContext
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
name|spring
operator|.
name|SpringCamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContextAware
import|;
end_import

begin_class
DECL|class|SpringCamelContextFactory
specifier|public
class|class
name|SpringCamelContextFactory
extends|extends
name|CamelContextFactory
implements|implements
name|ApplicationContextAware
block|{
DECL|field|applicationContext
specifier|private
name|ApplicationContext
name|applicationContext
decl_stmt|;
DECL|method|setApplicationContext (ApplicationContext context)
specifier|public
name|void
name|setApplicationContext
parameter_list|(
name|ApplicationContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|applicationContext
operator|=
name|context
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|newCamelContext ()
specifier|protected
name|DefaultCamelContext
name|newCamelContext
parameter_list|()
block|{
return|return
operator|new
name|SpringCamelContext
argument_list|(
name|applicationContext
argument_list|)
return|;
block|}
block|}
end_class

end_unit

