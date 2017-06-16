begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.ha
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ha
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
name|spi
operator|.
name|HasId
import|;
end_import

begin_interface
DECL|interface|CamelCluster
specifier|public
interface|interface
name|CamelCluster
extends|extends
name|HasId
block|{
comment|/**      * Creates a view of the cluster bound to a namespace.      *      * @param namespace the namespace the view refer to.      * @return the cluster view.      * @throws Exception if the view can't be created.      */
DECL|method|createView (String namespace)
name|CamelClusterView
name|createView
parameter_list|(
name|String
name|namespace
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

