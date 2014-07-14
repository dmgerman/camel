begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sparkrest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sparkrest
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
name|DefaultMessage
import|;
end_import

begin_import
import|import
name|spark
operator|.
name|Request
import|;
end_import

begin_import
import|import
name|spark
operator|.
name|Response
import|;
end_import

begin_comment
comment|/**  * Spark based {@link org.apache.camel.Message}.  *<p/>  * This implementation allows direct access to the Spark {@link Request} using  * the {@link #getRequest()} method.  */
end_comment

begin_class
DECL|class|SparkMessage
specifier|public
class|class
name|SparkMessage
extends|extends
name|DefaultMessage
block|{
DECL|field|request
specifier|private
specifier|final
specifier|transient
name|Request
name|request
decl_stmt|;
DECL|field|response
specifier|private
specifier|final
specifier|transient
name|Response
name|response
decl_stmt|;
DECL|method|SparkMessage (Request request, Response response)
specifier|public
name|SparkMessage
parameter_list|(
name|Request
name|request
parameter_list|,
name|Response
name|response
parameter_list|)
block|{
name|this
operator|.
name|request
operator|=
name|request
expr_stmt|;
name|this
operator|.
name|response
operator|=
name|response
expr_stmt|;
block|}
DECL|method|getRequest ()
specifier|public
name|Request
name|getRequest
parameter_list|()
block|{
return|return
name|request
return|;
block|}
DECL|method|getResponse ()
specifier|public
name|Response
name|getResponse
parameter_list|()
block|{
return|return
name|response
return|;
block|}
annotation|@
name|Override
DECL|method|newInstance ()
specifier|public
name|DefaultMessage
name|newInstance
parameter_list|()
block|{
return|return
operator|new
name|SparkMessage
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
return|;
block|}
block|}
end_class

end_unit

