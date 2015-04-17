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
name|spi
operator|.
name|UriParam
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
name|UriParams
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|SparkConfiguration
specifier|public
class|class
name|SparkConfiguration
block|{
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|mapHeaders
specifier|private
name|boolean
name|mapHeaders
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
DECL|field|disableStreamCache
specifier|private
name|boolean
name|disableStreamCache
decl_stmt|;
annotation|@
name|UriParam
DECL|field|urlDecodeHeaders
specifier|private
name|boolean
name|urlDecodeHeaders
decl_stmt|;
annotation|@
name|UriParam
DECL|field|transferException
specifier|private
name|boolean
name|transferException
decl_stmt|;
DECL|method|isMapHeaders ()
specifier|public
name|boolean
name|isMapHeaders
parameter_list|()
block|{
return|return
name|mapHeaders
return|;
block|}
comment|/**      * If this option is enabled, then during binding from Spark to Camel Message then the headers will be mapped as well      * (eg added as header to the Camel Message as well). You can turn off this option to disable this.      * The headers can still be accessed from the org.apache.camel.component.sparkrest.SparkMessage message with the      * method getRequest() that returns the Spark HTTP request instance.      */
DECL|method|setMapHeaders (boolean mapHeaders)
specifier|public
name|void
name|setMapHeaders
parameter_list|(
name|boolean
name|mapHeaders
parameter_list|)
block|{
name|this
operator|.
name|mapHeaders
operator|=
name|mapHeaders
expr_stmt|;
block|}
DECL|method|isDisableStreamCache ()
specifier|public
name|boolean
name|isDisableStreamCache
parameter_list|()
block|{
return|return
name|disableStreamCache
return|;
block|}
comment|/**      * Determines whether or not the raw input stream from Spark HttpRequest#getContent() is cached or not      * (Camel will read the stream into a in light-weight memory based Stream caching) cache.      * By default Camel will cache the Netty input stream to support reading it multiple times to ensure Camel      * can retrieve all data from the stream. However you can set this option to true when you for example need      * to access the raw stream, such as streaming it directly to a file or other persistent store.      * Mind that if you enable this option, then you cannot read the Netty stream multiple times out of the box,      * and you would need manually to reset the reader index on the Spark raw stream.      */
DECL|method|setDisableStreamCache (boolean disableStreamCache)
specifier|public
name|void
name|setDisableStreamCache
parameter_list|(
name|boolean
name|disableStreamCache
parameter_list|)
block|{
name|this
operator|.
name|disableStreamCache
operator|=
name|disableStreamCache
expr_stmt|;
block|}
DECL|method|isUrlDecodeHeaders ()
specifier|public
name|boolean
name|isUrlDecodeHeaders
parameter_list|()
block|{
return|return
name|urlDecodeHeaders
return|;
block|}
comment|/**      * If this option is enabled, then during binding from Spark to Camel Message then the header values will be URL decoded (eg %20 will be a space character.)      */
DECL|method|setUrlDecodeHeaders (boolean urlDecodeHeaders)
specifier|public
name|void
name|setUrlDecodeHeaders
parameter_list|(
name|boolean
name|urlDecodeHeaders
parameter_list|)
block|{
name|this
operator|.
name|urlDecodeHeaders
operator|=
name|urlDecodeHeaders
expr_stmt|;
block|}
DECL|method|isTransferException ()
specifier|public
name|boolean
name|isTransferException
parameter_list|()
block|{
return|return
name|transferException
return|;
block|}
comment|/**      * If enabled and an Exchange failed processing on the consumer side, and if the caused Exception was send back serialized      * in the response as a application/x-java-serialized-object content type.      */
DECL|method|setTransferException (boolean transferException)
specifier|public
name|void
name|setTransferException
parameter_list|(
name|boolean
name|transferException
parameter_list|)
block|{
name|this
operator|.
name|transferException
operator|=
name|transferException
expr_stmt|;
block|}
block|}
end_class

end_unit

