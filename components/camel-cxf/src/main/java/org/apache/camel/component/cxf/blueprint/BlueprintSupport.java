begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.blueprint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|blueprint
package|;
end_package

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

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|container
operator|.
name|BlueprintContainer
import|;
end_import

begin_interface
DECL|interface|BlueprintSupport
specifier|public
interface|interface
name|BlueprintSupport
block|{
DECL|method|setBlueprintContainer (BlueprintContainer blueprintContainer)
name|void
name|setBlueprintContainer
parameter_list|(
name|BlueprintContainer
name|blueprintContainer
parameter_list|)
function_decl|;
DECL|method|getBlueprintContainer ()
name|BlueprintContainer
name|getBlueprintContainer
parameter_list|()
function_decl|;
DECL|method|getBundleContext ()
name|BundleContext
name|getBundleContext
parameter_list|()
function_decl|;
DECL|method|setBundleContext (BundleContext bundleContext)
name|void
name|setBundleContext
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

