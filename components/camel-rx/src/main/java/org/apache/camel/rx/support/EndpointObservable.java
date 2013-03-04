begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.rx.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|rx
operator|.
name|support
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
name|Endpoint
import|;
end_import

begin_import
import|import
name|rx
operator|.
name|Observable
import|;
end_import

begin_import
import|import
name|rx
operator|.
name|Observer
import|;
end_import

begin_import
import|import
name|rx
operator|.
name|Subscription
import|;
end_import

begin_import
import|import
name|rx
operator|.
name|util
operator|.
name|functions
operator|.
name|Func1
import|;
end_import

begin_comment
comment|/**  * An {@link Observerable} Camel {@link Endpoint}  */
end_comment

begin_class
DECL|class|EndpointObservable
specifier|public
class|class
name|EndpointObservable
parameter_list|<
name|T
parameter_list|>
extends|extends
name|Observable
argument_list|<
name|T
argument_list|>
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|Endpoint
name|endpoint
decl_stmt|;
DECL|method|EndpointObservable (Endpoint endpoint, Func1<Observer<T>, Subscription> func)
specifier|public
name|EndpointObservable
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Func1
argument_list|<
name|Observer
argument_list|<
name|T
argument_list|>
argument_list|,
name|Subscription
argument_list|>
name|func
parameter_list|)
block|{
name|super
argument_list|(
name|func
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"EndpointObservable["
operator|+
name|endpoint
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

