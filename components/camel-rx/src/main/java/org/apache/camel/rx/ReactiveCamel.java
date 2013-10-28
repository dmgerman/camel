begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.rx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|rx
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
name|CamelContext
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
name|Endpoint
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
name|Message
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
name|rx
operator|.
name|support
operator|.
name|EndpointObservable
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
name|rx
operator|.
name|support
operator|.
name|EndpointSubscription
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
name|rx
operator|.
name|support
operator|.
name|ExchangeToBodyFunc1
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
name|rx
operator|.
name|support
operator|.
name|ExchangeToMessageFunc1
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
name|rx
operator|.
name|support
operator|.
name|ObserverSender
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
name|util
operator|.
name|CamelContextHelper
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
comment|/**  * Provides the<a href="https://rx.codeplex.com/">Reactive Extensions</a> support for  * Camel via the<a href="https://github.com/Netflix/RxJava/wiki">RxJava library</a>  */
end_comment

begin_class
DECL|class|ReactiveCamel
specifier|public
class|class
name|ReactiveCamel
block|{
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|method|ReactiveCamel (CamelContext camelContext)
specifier|public
name|ReactiveCamel
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
comment|/**      * Returns an {@link rx.Observable< org.apache.camel.Message>} to allow the messages sent on the endpoint      * to be processed using<a href="https://rx.codeplex.com/">Reactive Extensions</a>      */
DECL|method|toObservable (String uri)
specifier|public
name|Observable
argument_list|<
name|Message
argument_list|>
name|toObservable
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
name|toObservable
argument_list|(
name|endpoint
argument_list|(
name|uri
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Returns an {@link rx.Observable<T>} for the messages with their payload converted to the given type      * to allow the messages sent on the endpoint      * to be processed using<a href="https://rx.codeplex.com/">Reactive Extensions</a>      */
DECL|method|toObservable (String uri, final Class<T> bodyType)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Observable
argument_list|<
name|T
argument_list|>
name|toObservable
parameter_list|(
name|String
name|uri
parameter_list|,
specifier|final
name|Class
argument_list|<
name|T
argument_list|>
name|bodyType
parameter_list|)
block|{
return|return
name|toObservable
argument_list|(
name|endpoint
argument_list|(
name|uri
argument_list|)
argument_list|,
name|bodyType
argument_list|)
return|;
block|}
comment|/**      * Returns an {@link rx.Observable< org.apache.camel.Message>} to allow the messages sent on the endpoint      * to be processed using<a href="https://rx.codeplex.com/">Reactive Extensions</a>      */
DECL|method|toObservable (Endpoint endpoint)
specifier|public
name|Observable
argument_list|<
name|Message
argument_list|>
name|toObservable
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
return|return
name|createEndpointObservable
argument_list|(
name|endpoint
argument_list|,
name|ExchangeToMessageFunc1
operator|.
name|getInstance
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Returns an {@link rx.Observable<T>} for the messages with their payload converted to the given type      * to allow the messages sent on the endpoint      * to be processed using<a href="https://rx.codeplex.com/">Reactive Extensions</a>      */
DECL|method|toObservable (Endpoint endpoint, final Class<T> bodyType)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Observable
argument_list|<
name|T
argument_list|>
name|toObservable
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
specifier|final
name|Class
argument_list|<
name|T
argument_list|>
name|bodyType
parameter_list|)
block|{
return|return
name|createEndpointObservable
argument_list|(
name|endpoint
argument_list|,
operator|new
name|ExchangeToBodyFunc1
argument_list|<
name|T
argument_list|>
argument_list|(
name|bodyType
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Sends events on the given {@link Observable} to the given camel endpoint      */
DECL|method|sendTo (Observable<T> observable, String endpointUri)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|sendTo
parameter_list|(
name|Observable
argument_list|<
name|T
argument_list|>
name|observable
parameter_list|,
name|String
name|endpointUri
parameter_list|)
block|{
name|sendTo
argument_list|(
name|observable
argument_list|,
name|endpoint
argument_list|(
name|endpointUri
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sends events on the given {@link Observable} to the given camel endpoint      */
DECL|method|sendTo (Observable<T> observable, Endpoint endpoint)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|sendTo
parameter_list|(
name|Observable
argument_list|<
name|T
argument_list|>
name|observable
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
try|try
block|{
name|ObserverSender
argument_list|<
name|T
argument_list|>
name|observer
init|=
operator|new
name|ObserverSender
argument_list|<
name|T
argument_list|>
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|observable
operator|.
name|subscribe
argument_list|(
name|observer
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelRxException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|endpoint (String endpointUri)
specifier|public
name|Endpoint
name|endpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
return|return
name|CamelContextHelper
operator|.
name|getMandatoryEndpoint
argument_list|(
name|camelContext
argument_list|,
name|endpointUri
argument_list|)
return|;
block|}
comment|/**      * Returns a newly created {@link Observable} given a function which converts      * the {@link Exchange} from the Camel consumer to the required type      */
DECL|method|createEndpointObservable (final Endpoint endpoint, final Func1<Exchange, T> converter)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|Observable
argument_list|<
name|T
argument_list|>
name|createEndpointObservable
parameter_list|(
specifier|final
name|Endpoint
name|endpoint
parameter_list|,
specifier|final
name|Func1
argument_list|<
name|Exchange
argument_list|,
name|T
argument_list|>
name|converter
parameter_list|)
block|{
name|Observable
operator|.
name|OnSubscribeFunc
argument_list|<
name|T
argument_list|>
name|func
init|=
operator|new
name|Observable
operator|.
name|OnSubscribeFunc
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Subscription
name|onSubscribe
parameter_list|(
name|Observer
argument_list|<
name|?
super|super
name|T
argument_list|>
name|observer
parameter_list|)
block|{
return|return
operator|new
name|EndpointSubscription
argument_list|<
name|T
argument_list|>
argument_list|(
name|endpoint
argument_list|,
name|observer
argument_list|,
name|converter
argument_list|)
return|;
block|}
block|}
decl_stmt|;
return|return
operator|new
name|EndpointObservable
argument_list|<
name|T
argument_list|>
argument_list|(
name|endpoint
argument_list|,
name|func
argument_list|)
return|;
block|}
block|}
end_class

end_unit

