begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.beanstalk
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|beanstalk
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLDecoder
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Scanner
import|;
end_import

begin_import
import|import
name|com
operator|.
name|surftools
operator|.
name|BeanstalkClient
operator|.
name|Client
import|;
end_import

begin_import
import|import
name|com
operator|.
name|surftools
operator|.
name|BeanstalkClientImpl
operator|.
name|ClientImpl
import|;
end_import

begin_comment
comment|/**  * Represents the connection to Beanstalk.  *<p/>  * Along with the list of tubes it may watch.  */
end_comment

begin_class
DECL|class|ConnectionSettings
specifier|public
class|class
name|ConnectionSettings
block|{
DECL|field|host
specifier|final
name|String
name|host
decl_stmt|;
DECL|field|port
specifier|final
name|int
name|port
decl_stmt|;
DECL|field|tubes
specifier|final
name|String
index|[]
name|tubes
decl_stmt|;
DECL|method|ConnectionSettings (final String tube)
specifier|public
name|ConnectionSettings
parameter_list|(
specifier|final
name|String
name|tube
parameter_list|)
block|{
name|this
argument_list|(
name|Client
operator|.
name|DEFAULT_HOST
argument_list|,
name|Client
operator|.
name|DEFAULT_PORT
argument_list|,
name|tube
argument_list|)
expr_stmt|;
block|}
DECL|method|ConnectionSettings (final String host, final String tube)
specifier|public
name|ConnectionSettings
parameter_list|(
specifier|final
name|String
name|host
parameter_list|,
specifier|final
name|String
name|tube
parameter_list|)
block|{
name|this
argument_list|(
name|host
argument_list|,
name|Client
operator|.
name|DEFAULT_PORT
argument_list|,
name|tube
argument_list|)
expr_stmt|;
block|}
DECL|method|ConnectionSettings (final String host, final int port, final String tube)
specifier|public
name|ConnectionSettings
parameter_list|(
specifier|final
name|String
name|host
parameter_list|,
specifier|final
name|int
name|port
parameter_list|,
specifier|final
name|String
name|tube
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
specifier|final
name|Scanner
name|scanner
init|=
operator|new
name|Scanner
argument_list|(
name|tube
argument_list|)
decl_stmt|;
name|scanner
operator|.
name|useDelimiter
argument_list|(
literal|"\\+"
argument_list|)
expr_stmt|;
specifier|final
name|ArrayList
argument_list|<
name|String
argument_list|>
name|buffer
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
while|while
condition|(
name|scanner
operator|.
name|hasNext
argument_list|()
condition|)
block|{
specifier|final
name|String
name|tubeRaw
init|=
name|scanner
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|buffer
operator|.
name|add
argument_list|(
name|URLDecoder
operator|.
name|decode
argument_list|(
name|tubeRaw
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
name|buffer
operator|.
name|add
argument_list|(
name|tubeRaw
argument_list|)
expr_stmt|;
block|}
block|}
name|this
operator|.
name|tubes
operator|=
name|buffer
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|buffer
operator|.
name|size
argument_list|()
index|]
argument_list|)
expr_stmt|;
name|scanner
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns the {@link Client} instance ready for writing      * operations, e.g. "put".      *<p/>      *<code>use(tube)</code> is applied during this call.      *      * @return {@link Client} instance      * @throws IllegalArgumentException the exception is raised when this ConnectionSettings      *                                  has more than one tube.      */
DECL|method|newWritingClient ()
specifier|public
name|Client
name|newWritingClient
parameter_list|()
throws|throws
name|IllegalArgumentException
block|{
if|if
condition|(
name|tubes
operator|.
name|length
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"There must be only one tube specified for Beanstalk producer"
argument_list|)
throw|;
block|}
specifier|final
name|String
name|tube
init|=
name|tubes
operator|.
name|length
operator|>
literal|0
condition|?
name|tubes
index|[
literal|0
index|]
else|:
name|BeanstalkComponent
operator|.
name|DEFAULT_TUBE
decl_stmt|;
specifier|final
name|ClientImpl
name|client
init|=
operator|new
name|ClientImpl
argument_list|(
name|host
argument_list|,
name|port
argument_list|)
decl_stmt|;
comment|/* FIXME: There is a problem in JavaBeanstalkClient 1.4.4 (at least in 1.4.4),            when using uniqueConnectionPerThread=false. The symptom is that ProtocolHandler            breaks the protocol, reading incomplete messages. To be investigated. */
comment|//client.setUniqueConnectionPerThread(false);
name|client
operator|.
name|useTube
argument_list|(
name|tube
argument_list|)
expr_stmt|;
return|return
name|client
return|;
block|}
comment|/**      * Returns the {@link Client} instance for reading operations with all      * the tubes aleady watched      *<p/>      *<code>watch(tube)</code> is applied for every tube during this call.      *      * @param useBlockIO configuration param to {@link Client}      * @return {@link Client} instance      */
DECL|method|newReadingClient (boolean useBlockIO)
specifier|public
name|Client
name|newReadingClient
parameter_list|(
name|boolean
name|useBlockIO
parameter_list|)
block|{
specifier|final
name|ClientImpl
name|client
init|=
operator|new
name|ClientImpl
argument_list|(
name|host
argument_list|,
name|port
argument_list|,
name|useBlockIO
argument_list|)
decl_stmt|;
comment|/* FIXME: There is a problem in JavaBeanstalkClient 1.4.4 (at least in 1.4.4),            when using uniqueConnectionPerThread=false. The symptom is that ProtocolHandler            breaks the protocol, reading incomplete messages. To be investigated. */
comment|//client.setUniqueConnectionPerThread(false);
for|for
control|(
name|String
name|tube
range|:
name|tubes
control|)
block|{
name|client
operator|.
name|watch
argument_list|(
name|tube
argument_list|)
expr_stmt|;
block|}
return|return
name|client
return|;
block|}
annotation|@
name|Override
DECL|method|equals (final Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
specifier|final
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|ConnectionSettings
condition|)
block|{
specifier|final
name|ConnectionSettings
name|other
init|=
operator|(
name|ConnectionSettings
operator|)
name|obj
decl_stmt|;
return|return
name|other
operator|.
name|host
operator|.
name|equals
argument_list|(
name|host
argument_list|)
operator|&&
name|other
operator|.
name|port
operator|==
name|port
operator|&&
name|Arrays
operator|.
name|equals
argument_list|(
name|other
operator|.
name|tubes
argument_list|,
name|tubes
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
literal|41
operator|*
operator|(
literal|41
operator|*
operator|(
literal|41
operator|+
name|host
operator|.
name|hashCode
argument_list|()
operator|)
operator|+
name|port
operator|)
operator|+
name|Arrays
operator|.
name|hashCode
argument_list|(
name|tubes
argument_list|)
return|;
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
literal|"beanstalk://"
operator|+
name|host
operator|+
literal|":"
operator|+
name|port
operator|+
literal|"/"
operator|+
name|Arrays
operator|.
name|toString
argument_list|(
name|tubes
argument_list|)
return|;
block|}
block|}
end_class

end_unit

