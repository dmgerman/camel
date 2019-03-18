begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.opentracing.propagation
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|opentracing
operator|.
name|propagation
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

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
name|io
operator|.
name|opentracing
operator|.
name|propagation
operator|.
name|TextMap
import|;
end_import

begin_class
DECL|class|CamelMessagingHeadersInjectAdapter
specifier|public
specifier|final
class|class
name|CamelMessagingHeadersInjectAdapter
implements|implements
name|TextMap
block|{
comment|// As per the JMS specs, header names must be valid Java identifier part
comment|// characters.
comment|// This means that any header names that contain illegal characters (- for
comment|// example) should be handled correctly
comment|// Opentracing java-jms does it as follows.
DECL|field|JMS_DASH
specifier|static
specifier|final
name|String
name|JMS_DASH
init|=
literal|"_$dash$_"
decl_stmt|;
DECL|field|map
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
decl_stmt|;
DECL|field|jmsEncoding
specifier|private
specifier|final
name|boolean
name|jmsEncoding
decl_stmt|;
DECL|method|CamelMessagingHeadersInjectAdapter (final Map<String, Object> map, boolean jmsEncoding)
specifier|public
name|CamelMessagingHeadersInjectAdapter
parameter_list|(
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|,
name|boolean
name|jmsEncoding
parameter_list|)
block|{
name|this
operator|.
name|map
operator|=
name|map
expr_stmt|;
name|this
operator|.
name|jmsEncoding
operator|=
name|jmsEncoding
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|iterator ()
specifier|public
name|Iterator
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|iterator
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"CamelHeadersInjectAdapter should only be used with Tracer.inject()"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|put (String key, String value)
specifier|public
name|void
name|put
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
comment|// Assume any header property that begins with 'Camel' is for internal
comment|// use
if|if
condition|(
operator|!
name|key
operator|.
name|startsWith
argument_list|(
literal|"Camel"
argument_list|)
condition|)
block|{
name|this
operator|.
name|map
operator|.
name|put
argument_list|(
name|encodeDash
argument_list|(
name|key
argument_list|)
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Encode all dashes because JMS specification doesn't allow them in      * property name      */
DECL|method|encodeDash (String key)
specifier|private
name|String
name|encodeDash
parameter_list|(
name|String
name|key
parameter_list|)
block|{
if|if
condition|(
name|key
operator|==
literal|null
operator|||
name|key
operator|.
name|isEmpty
argument_list|()
operator|||
operator|!
name|jmsEncoding
condition|)
block|{
return|return
name|key
return|;
block|}
return|return
name|key
operator|.
name|replace
argument_list|(
literal|"-"
argument_list|,
name|JMS_DASH
argument_list|)
return|;
block|}
block|}
end_class

end_unit

