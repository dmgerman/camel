begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jetty
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
name|Converter
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
name|FallbackConverter
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
name|StreamCache
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
name|TypeConverter
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
name|TypeConverterRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|Converter
DECL|class|JettyConverter
specifier|public
specifier|final
class|class
name|JettyConverter
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|JettyConverter
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|JettyConverter ()
specifier|private
name|JettyConverter
parameter_list|()
block|{     }
annotation|@
name|FallbackConverter
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|convertTo (Class<?> type, Exchange exchange, Object value, TypeConverterRegistry registry)
specifier|public
specifier|static
name|Object
name|convertTo
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
name|value
parameter_list|,
name|TypeConverterRegistry
name|registry
parameter_list|)
throws|throws
name|Exception
block|{
comment|// do not convert to stream cache
if|if
condition|(
name|StreamCache
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|value
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|JettyHttpMessage
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|value
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|JettyHttpMessage
name|message
init|=
operator|(
name|JettyHttpMessage
operator|)
name|value
decl_stmt|;
name|Object
name|body
init|=
name|message
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|body
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|body
operator|instanceof
name|JettyFutureGetBody
condition|)
block|{
name|JettyFutureGetBody
name|future
init|=
operator|(
name|JettyFutureGetBody
operator|)
name|body
decl_stmt|;
if|if
condition|(
name|future
operator|.
name|isCancelled
argument_list|()
condition|)
block|{
comment|// return void to indicate its not possible to convert at this time
return|return
name|Void
operator|.
name|TYPE
return|;
block|}
comment|// do some trace logging as the get is blocking until the response is ready
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Getting future response"
argument_list|)
expr_stmt|;
block|}
name|Object
name|reply
init|=
name|future
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Got future response"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|reply
operator|==
literal|null
condition|)
block|{
comment|// return void to indicate its not possible to convert at this time
return|return
name|Void
operator|.
name|TYPE
return|;
block|}
comment|// maybe from is already the type we want
if|if
condition|(
name|type
operator|.
name|isAssignableFrom
argument_list|(
name|reply
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
name|reply
argument_list|)
return|;
block|}
comment|// no then try to lookup a type converter
name|TypeConverter
name|tc
init|=
name|registry
operator|.
name|lookup
argument_list|(
name|type
argument_list|,
name|reply
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|tc
operator|!=
literal|null
condition|)
block|{
return|return
name|tc
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|exchange
argument_list|,
name|reply
argument_list|)
return|;
block|}
block|}
else|else
block|{
comment|// no then try to lookup a type converter
name|TypeConverter
name|tc
init|=
name|registry
operator|.
name|lookup
argument_list|(
name|type
argument_list|,
name|body
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|tc
operator|!=
literal|null
condition|)
block|{
return|return
name|tc
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|exchange
argument_list|,
name|body
argument_list|)
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

