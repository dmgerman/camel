begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms.discovery
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
operator|.
name|discovery
package|;
end_package

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
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_comment
comment|/**  * A simple POJO showing how to create a simple registry  *   * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|MyRegistry
specifier|public
class|class
name|MyRegistry
block|{
DECL|field|services
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|>
name|services
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Map
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|onEvent (Map heartbeat)
specifier|public
name|void
name|onEvent
parameter_list|(
name|Map
name|heartbeat
parameter_list|)
block|{
name|String
name|key
init|=
operator|(
name|String
operator|)
name|heartbeat
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
name|services
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|heartbeat
argument_list|)
expr_stmt|;
block|}
DECL|method|getServices ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|>
name|getServices
parameter_list|()
block|{
return|return
name|services
return|;
block|}
block|}
end_class

end_unit

