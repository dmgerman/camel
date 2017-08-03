begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
operator|.
name|cloud
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
name|test
operator|.
name|AvailablePortFinder
import|;
end_import

begin_class
DECL|class|SpringBootPropertyUtil
specifier|public
specifier|final
class|class
name|SpringBootPropertyUtil
block|{
DECL|field|PORT1
specifier|public
specifier|static
specifier|final
name|int
name|PORT1
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
DECL|field|PORT2
specifier|public
specifier|static
specifier|final
name|int
name|PORT2
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
DECL|field|PORT3
specifier|public
specifier|static
specifier|final
name|int
name|PORT3
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
DECL|field|HOSTNAME
specifier|public
specifier|static
specifier|final
name|String
name|HOSTNAME
init|=
literal|"localhost:"
decl_stmt|;
DECL|method|SpringBootPropertyUtil ()
specifier|private
name|SpringBootPropertyUtil
parameter_list|()
block|{      }
DECL|method|getDiscoveryServices ()
specifier|public
specifier|static
name|String
name|getDiscoveryServices
parameter_list|()
block|{
name|StringBuffer
name|dservice
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|dservice
operator|.
name|append
argument_list|(
name|HOSTNAME
argument_list|)
expr_stmt|;
name|dservice
operator|.
name|append
argument_list|(
name|PORT1
argument_list|)
expr_stmt|;
name|dservice
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|dservice
operator|.
name|append
argument_list|(
name|HOSTNAME
argument_list|)
expr_stmt|;
name|dservice
operator|.
name|append
argument_list|(
name|PORT2
argument_list|)
expr_stmt|;
name|dservice
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|dservice
operator|.
name|append
argument_list|(
name|HOSTNAME
argument_list|)
expr_stmt|;
name|dservice
operator|.
name|append
argument_list|(
name|PORT3
argument_list|)
expr_stmt|;
return|return
name|dservice
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|getServiceFilterBlacklist ()
specifier|public
specifier|static
name|String
name|getServiceFilterBlacklist
parameter_list|()
block|{
name|StringBuffer
name|filterBlacklistString
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|filterBlacklistString
operator|.
name|append
argument_list|(
name|HOSTNAME
argument_list|)
expr_stmt|;
name|filterBlacklistString
operator|.
name|append
argument_list|(
name|PORT2
argument_list|)
expr_stmt|;
return|return
name|filterBlacklistString
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

