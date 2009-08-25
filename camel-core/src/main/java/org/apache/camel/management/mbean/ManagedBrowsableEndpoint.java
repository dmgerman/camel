begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
operator|.
name|mbean
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
name|Exchange
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
name|BrowsableEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jmx
operator|.
name|export
operator|.
name|annotation
operator|.
name|ManagedOperation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jmx
operator|.
name|export
operator|.
name|annotation
operator|.
name|ManagedResource
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed BrowsableEndpoint"
argument_list|)
DECL|class|ManagedBrowsableEndpoint
specifier|public
class|class
name|ManagedBrowsableEndpoint
extends|extends
name|ManagedEndpoint
block|{
DECL|field|endpoint
specifier|private
name|BrowsableEndpoint
name|endpoint
decl_stmt|;
DECL|method|ManagedBrowsableEndpoint (BrowsableEndpoint endpoint)
specifier|public
name|ManagedBrowsableEndpoint
parameter_list|(
name|BrowsableEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
DECL|method|getEndpoint ()
specifier|public
name|BrowsableEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|endpoint
return|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Current number of Exchanges in Queue"
argument_list|)
DECL|method|qeueSize ()
specifier|public
name|long
name|qeueSize
parameter_list|()
block|{
return|return
name|endpoint
operator|.
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Get Exchange from queue by index"
argument_list|)
DECL|method|browseExchange (Integer index)
specifier|public
name|Exchange
name|browseExchange
parameter_list|(
name|Integer
name|index
parameter_list|)
block|{
return|return
name|endpoint
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
name|index
argument_list|)
return|;
block|}
block|}
end_class

end_unit

