begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ribbon.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ribbon
operator|.
name|processor
package|;
end_package

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|loadbalancer
operator|.
name|Server
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
name|ServiceCallServer
import|;
end_import

begin_class
DECL|class|RibbonServer
specifier|public
class|class
name|RibbonServer
extends|extends
name|Server
implements|implements
name|ServiceCallServer
block|{
DECL|method|RibbonServer (String host, int port)
specifier|public
name|RibbonServer
parameter_list|(
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|)
block|{
name|super
argument_list|(
name|host
argument_list|,
name|port
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getIp ()
specifier|public
name|String
name|getIp
parameter_list|()
block|{
return|return
name|getHost
argument_list|()
return|;
block|}
block|}
end_class

end_unit

