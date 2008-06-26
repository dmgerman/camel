begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.interceptor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|interceptor
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
name|util
operator|.
name|ObjectHelper
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
name|UnitOfWork
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|TraceFormatter
specifier|public
class|class
name|TraceFormatter
block|{
DECL|field|showBreadCrumb
specifier|private
name|boolean
name|showBreadCrumb
init|=
literal|true
decl_stmt|;
DECL|field|showNode
specifier|private
name|boolean
name|showNode
init|=
literal|true
decl_stmt|;
DECL|field|showExchangeId
specifier|private
name|boolean
name|showExchangeId
decl_stmt|;
DECL|field|showProperties
specifier|private
name|boolean
name|showProperties
init|=
literal|true
decl_stmt|;
DECL|field|showHeaders
specifier|private
name|boolean
name|showHeaders
init|=
literal|true
decl_stmt|;
DECL|field|showBody
specifier|private
name|boolean
name|showBody
init|=
literal|true
decl_stmt|;
DECL|field|showBodyType
specifier|private
name|boolean
name|showBodyType
init|=
literal|true
decl_stmt|;
DECL|method|format (TraceInterceptor interceptor, Exchange exchange)
specifier|public
name|Object
name|format
parameter_list|(
name|TraceInterceptor
name|interceptor
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|Throwable
name|exception
init|=
name|exchange
operator|.
name|getException
argument_list|()
decl_stmt|;
return|return
operator|(
name|showBreadCrumb
condition|?
name|getBreadCrumbID
argument_list|(
name|exchange
argument_list|)
operator|+
literal|" "
else|:
literal|""
operator|)
operator|+
literal|"-> "
operator|+
name|getNodeMessage
argument_list|(
name|interceptor
argument_list|)
operator|+
literal|" "
operator|+
operator|(
name|showNode
condition|?
name|interceptor
operator|.
name|getNode
argument_list|()
operator|+
literal|" "
else|:
literal|""
operator|)
operator|+
name|exchange
operator|.
name|getPattern
argument_list|()
operator|+
operator|(
name|showExchangeId
condition|?
literal|" Id: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
else|:
literal|""
operator|)
operator|+
operator|(
name|showProperties
condition|?
literal|" Properties:"
operator|+
name|exchange
operator|.
name|getProperties
argument_list|()
else|:
literal|""
operator|)
operator|+
operator|(
name|showHeaders
condition|?
literal|" Headers:"
operator|+
name|in
operator|.
name|getHeaders
argument_list|()
else|:
literal|""
operator|)
operator|+
operator|(
name|showBodyType
condition|?
literal|" BodyType:"
operator|+
name|ObjectHelper
operator|.
name|className
argument_list|(
name|in
operator|.
name|getBody
argument_list|()
argument_list|)
else|:
literal|""
operator|)
operator|+
operator|(
name|showBody
condition|?
literal|" Body:"
operator|+
name|getBodyAsString
argument_list|(
name|in
argument_list|)
else|:
literal|""
operator|)
operator|+
operator|(
name|exception
operator|!=
literal|null
condition|?
literal|" Exception: "
operator|+
name|exception
else|:
literal|""
operator|)
return|;
block|}
DECL|method|isShowBody ()
specifier|public
name|boolean
name|isShowBody
parameter_list|()
block|{
return|return
name|showBody
return|;
block|}
DECL|method|setShowBody (boolean showBody)
specifier|public
name|void
name|setShowBody
parameter_list|(
name|boolean
name|showBody
parameter_list|)
block|{
name|this
operator|.
name|showBody
operator|=
name|showBody
expr_stmt|;
block|}
DECL|method|isShowBodyType ()
specifier|public
name|boolean
name|isShowBodyType
parameter_list|()
block|{
return|return
name|showBodyType
return|;
block|}
DECL|method|setShowBodyType (boolean showBodyType)
specifier|public
name|void
name|setShowBodyType
parameter_list|(
name|boolean
name|showBodyType
parameter_list|)
block|{
name|this
operator|.
name|showBodyType
operator|=
name|showBodyType
expr_stmt|;
block|}
DECL|method|isShowBreadCrumb ()
specifier|public
name|boolean
name|isShowBreadCrumb
parameter_list|()
block|{
return|return
name|showBreadCrumb
return|;
block|}
DECL|method|setShowBreadCrumb (boolean showBreadCrumb)
specifier|public
name|void
name|setShowBreadCrumb
parameter_list|(
name|boolean
name|showBreadCrumb
parameter_list|)
block|{
name|this
operator|.
name|showBreadCrumb
operator|=
name|showBreadCrumb
expr_stmt|;
block|}
DECL|method|isShowExchangeId ()
specifier|public
name|boolean
name|isShowExchangeId
parameter_list|()
block|{
return|return
name|showExchangeId
return|;
block|}
DECL|method|setShowExchangeId (boolean showExchangeId)
specifier|public
name|void
name|setShowExchangeId
parameter_list|(
name|boolean
name|showExchangeId
parameter_list|)
block|{
name|this
operator|.
name|showExchangeId
operator|=
name|showExchangeId
expr_stmt|;
block|}
DECL|method|isShowHeaders ()
specifier|public
name|boolean
name|isShowHeaders
parameter_list|()
block|{
return|return
name|showHeaders
return|;
block|}
DECL|method|setShowHeaders (boolean showHeaders)
specifier|public
name|void
name|setShowHeaders
parameter_list|(
name|boolean
name|showHeaders
parameter_list|)
block|{
name|this
operator|.
name|showHeaders
operator|=
name|showHeaders
expr_stmt|;
block|}
DECL|method|isShowProperties ()
specifier|public
name|boolean
name|isShowProperties
parameter_list|()
block|{
return|return
name|showProperties
return|;
block|}
DECL|method|setShowProperties (boolean showProperties)
specifier|public
name|void
name|setShowProperties
parameter_list|(
name|boolean
name|showProperties
parameter_list|)
block|{
name|this
operator|.
name|showProperties
operator|=
name|showProperties
expr_stmt|;
block|}
DECL|method|isShowNode ()
specifier|public
name|boolean
name|isShowNode
parameter_list|()
block|{
return|return
name|showNode
return|;
block|}
DECL|method|setShowNode (boolean showNode)
specifier|public
name|void
name|setShowNode
parameter_list|(
name|boolean
name|showNode
parameter_list|)
block|{
name|this
operator|.
name|showNode
operator|=
name|showNode
expr_stmt|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
DECL|method|getBreadCrumbID (Exchange exchange)
specifier|protected
name|Object
name|getBreadCrumbID
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|UnitOfWork
name|unitOfWork
init|=
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
decl_stmt|;
return|return
name|unitOfWork
operator|.
name|getId
argument_list|()
return|;
block|}
DECL|method|getBodyAsString (Message in)
specifier|protected
name|Object
name|getBodyAsString
parameter_list|(
name|Message
name|in
parameter_list|)
block|{
name|Object
name|answer
init|=
name|in
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|in
operator|.
name|getBody
argument_list|()
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|getNodeMessage (TraceInterceptor interceptor)
specifier|protected
name|String
name|getNodeMessage
parameter_list|(
name|TraceInterceptor
name|interceptor
parameter_list|)
block|{
return|return
name|interceptor
operator|.
name|getNode
argument_list|()
operator|.
name|idOrCreate
argument_list|()
return|;
block|}
block|}
end_class

end_unit

