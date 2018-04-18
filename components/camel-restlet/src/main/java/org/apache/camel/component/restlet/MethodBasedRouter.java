begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.restlet
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|restlet
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
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicBoolean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|Request
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|Restlet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|data
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|data
operator|.
name|Status
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * A simple router that routes requests to target Restlets based on method.   *   * @version   */
end_comment

begin_class
DECL|class|MethodBasedRouter
class|class
name|MethodBasedRouter
extends|extends
name|Restlet
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MethodBasedRouter
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|uriPattern
specifier|private
name|String
name|uriPattern
decl_stmt|;
DECL|field|routes
specifier|private
name|Map
argument_list|<
name|Method
argument_list|,
name|Restlet
argument_list|>
name|routes
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|hasBeenAttachedFlag
specifier|private
name|AtomicBoolean
name|hasBeenAttachedFlag
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
DECL|method|MethodBasedRouter (String uriPattern)
name|MethodBasedRouter
parameter_list|(
name|String
name|uriPattern
parameter_list|)
block|{
name|this
operator|.
name|uriPattern
operator|=
name|uriPattern
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|handle (Request request, Response response)
specifier|public
name|void
name|handle
parameter_list|(
name|Request
name|request
parameter_list|,
name|Response
name|response
parameter_list|)
block|{
name|Method
name|method
init|=
name|request
operator|.
name|getMethod
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"MethodRouter ({}) received request method: {}"
argument_list|,
name|uriPattern
argument_list|,
name|method
argument_list|)
expr_stmt|;
name|Restlet
name|target
init|=
name|routes
operator|.
name|get
argument_list|(
name|method
argument_list|)
decl_stmt|;
if|if
condition|(
name|target
operator|==
literal|null
operator|||
name|org
operator|.
name|restlet
operator|.
name|data
operator|.
name|Method
operator|.
name|OPTIONS
operator|.
name|equals
argument_list|(
name|method
argument_list|)
condition|)
block|{
comment|// must include list of allowed methods
name|response
operator|.
name|setAllowedMethods
argument_list|(
name|routes
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|target
operator|!=
literal|null
condition|)
block|{
name|target
operator|.
name|handle
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"MethodRouter ({}) method not allowed: {}"
argument_list|,
name|uriPattern
argument_list|,
name|method
argument_list|)
expr_stmt|;
name|response
operator|.
name|setStatus
argument_list|(
name|Status
operator|.
name|CLIENT_ERROR_METHOD_NOT_ALLOWED
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|addRoute (Method method, Restlet target)
name|void
name|addRoute
parameter_list|(
name|Method
name|method
parameter_list|,
name|Restlet
name|target
parameter_list|)
block|{
name|routes
operator|.
name|put
argument_list|(
name|method
argument_list|,
name|target
argument_list|)
expr_stmt|;
block|}
DECL|method|removeRoute (Method method)
name|void
name|removeRoute
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
name|routes
operator|.
name|remove
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
DECL|method|hasRoutes ()
name|boolean
name|hasRoutes
parameter_list|()
block|{
return|return
operator|!
name|routes
operator|.
name|isEmpty
argument_list|()
return|;
block|}
comment|/**      * This method does "test-and-set" on the underlying flag that indicates      * whether this router restlet has been attached to a server or not.  It       * is the caller's responsibility to perform the "attach" when this method       * returns false.       *       * @return true only this method is called the first time.      */
DECL|method|hasBeenAttached ()
name|boolean
name|hasBeenAttached
parameter_list|()
block|{
return|return
name|hasBeenAttachedFlag
operator|.
name|getAndSet
argument_list|(
literal|true
argument_list|)
return|;
block|}
DECL|method|getUriPattern ()
name|String
name|getUriPattern
parameter_list|()
block|{
return|return
name|uriPattern
return|;
block|}
block|}
end_class

end_unit

