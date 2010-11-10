begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.core.osgi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|core
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
name|DefaultCamelContextNameStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_class
DECL|class|OsgiCamelContextNameStrategy
specifier|public
class|class
name|OsgiCamelContextNameStrategy
extends|extends
name|DefaultCamelContextNameStrategy
block|{
DECL|method|OsgiCamelContextNameStrategy (BundleContext context)
specifier|public
name|OsgiCamelContextNameStrategy
parameter_list|(
name|BundleContext
name|context
parameter_list|)
block|{
comment|// use bundle id in auto assigned names to make it unique
name|super
argument_list|(
literal|"camel-"
operator|+
name|context
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
operator|+
literal|"-"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

