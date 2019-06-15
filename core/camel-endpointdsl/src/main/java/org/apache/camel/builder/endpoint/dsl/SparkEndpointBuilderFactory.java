begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.endpoint.dsl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|endpoint
operator|.
name|dsl
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|builder
operator|.
name|EndpointConsumerBuilder
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
name|builder
operator|.
name|EndpointProducerBuilder
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
name|builder
operator|.
name|endpoint
operator|.
name|AbstractEndpointBuilder
import|;
end_import

begin_comment
comment|/**  * The spark-rest component is used for hosting REST services which has been  * defined using Camel rest-dsl.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|SparkEndpointBuilderFactory
specifier|public
interface|interface
name|SparkEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Spark Rest component.      */
DECL|interface|SparkEndpointBuilder
specifier|public
specifier|static
interface|interface
name|SparkEndpointBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedSparkEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedSparkEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * get, post, put, patch, delete, head, trace, connect, or options.          * The option is a<code>java.lang.String</code> type.          * @group consumer          */
DECL|method|verb (String verb)
specifier|default
name|SparkEndpointBuilder
name|verb
parameter_list|(
name|String
name|verb
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"verb"
argument_list|,
name|verb
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The content path which support Spark syntax.          * The option is a<code>java.lang.String</code> type.          * @group consumer          */
DECL|method|path (String path)
specifier|default
name|SparkEndpointBuilder
name|path
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"path"
argument_list|,
name|path
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Accept type such as: 'text/xml', or 'application/json'. By default we          * accept all kinds of types.          * The option is a<code>java.lang.String</code> type.          * @group consumer          */
DECL|method|accept (String accept)
specifier|default
name|SparkEndpointBuilder
name|accept
parameter_list|(
name|String
name|accept
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"accept"
argument_list|,
name|accept
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Determines whether or not the raw input stream from Spark          * HttpRequest#getContent() is cached or not (Camel will read the stream          * into a in light-weight memory based Stream caching) cache. By default          * Camel will cache the Netty input stream to support reading it          * multiple times to ensure Camel can retrieve all data from the stream.          * However you can set this option to true when you for example need to          * access the raw stream, such as streaming it directly to a file or          * other persistent store. Mind that if you enable this option, then you          * cannot read the Netty stream multiple times out of the box, and you          * would need manually to reset the reader index on the Spark raw          * stream.          * The option is a<code>boolean</code> type.          * @group consumer          */
DECL|method|disableStreamCache ( boolean disableStreamCache)
specifier|default
name|SparkEndpointBuilder
name|disableStreamCache
parameter_list|(
name|boolean
name|disableStreamCache
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"disableStreamCache"
argument_list|,
name|disableStreamCache
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Determines whether or not the raw input stream from Spark          * HttpRequest#getContent() is cached or not (Camel will read the stream          * into a in light-weight memory based Stream caching) cache. By default          * Camel will cache the Netty input stream to support reading it          * multiple times to ensure Camel can retrieve all data from the stream.          * However you can set this option to true when you for example need to          * access the raw stream, such as streaming it directly to a file or          * other persistent store. Mind that if you enable this option, then you          * cannot read the Netty stream multiple times out of the box, and you          * would need manually to reset the reader index on the Spark raw          * stream.          * The option will be converted to a<code>boolean</code> type.          * @group consumer          */
DECL|method|disableStreamCache ( String disableStreamCache)
specifier|default
name|SparkEndpointBuilder
name|disableStreamCache
parameter_list|(
name|String
name|disableStreamCache
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"disableStreamCache"
argument_list|,
name|disableStreamCache
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If this option is enabled, then during binding from Spark to Camel          * Message then the headers will be mapped as well (eg added as header          * to the Camel Message as well). You can turn off this option to          * disable this. The headers can still be accessed from the          * org.apache.camel.component.sparkrest.SparkMessage message with the          * method getRequest() that returns the Spark HTTP request instance.          * The option is a<code>boolean</code> type.          * @group consumer          */
DECL|method|mapHeaders (boolean mapHeaders)
specifier|default
name|SparkEndpointBuilder
name|mapHeaders
parameter_list|(
name|boolean
name|mapHeaders
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"mapHeaders"
argument_list|,
name|mapHeaders
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If this option is enabled, then during binding from Spark to Camel          * Message then the headers will be mapped as well (eg added as header          * to the Camel Message as well). You can turn off this option to          * disable this. The headers can still be accessed from the          * org.apache.camel.component.sparkrest.SparkMessage message with the          * method getRequest() that returns the Spark HTTP request instance.          * The option will be converted to a<code>boolean</code> type.          * @group consumer          */
DECL|method|mapHeaders (String mapHeaders)
specifier|default
name|SparkEndpointBuilder
name|mapHeaders
parameter_list|(
name|String
name|mapHeaders
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"mapHeaders"
argument_list|,
name|mapHeaders
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If enabled and an Exchange failed processing on the consumer side,          * and if the caused Exception was send back serialized in the response          * as a application/x-java-serialized-object content type. This is by          * default turned off. If you enable this then be aware that Java will          * deserialize the incoming data from the request to Java and that can          * be a potential security risk.          * The option is a<code>boolean</code> type.          * @group consumer          */
DECL|method|transferException (boolean transferException)
specifier|default
name|SparkEndpointBuilder
name|transferException
parameter_list|(
name|boolean
name|transferException
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"transferException"
argument_list|,
name|transferException
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If enabled and an Exchange failed processing on the consumer side,          * and if the caused Exception was send back serialized in the response          * as a application/x-java-serialized-object content type. This is by          * default turned off. If you enable this then be aware that Java will          * deserialize the incoming data from the request to Java and that can          * be a potential security risk.          * The option will be converted to a<code>boolean</code> type.          * @group consumer          */
DECL|method|transferException (String transferException)
specifier|default
name|SparkEndpointBuilder
name|transferException
parameter_list|(
name|String
name|transferException
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"transferException"
argument_list|,
name|transferException
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If this option is enabled, then during binding from Spark to Camel          * Message then the header values will be URL decoded (eg %20 will be a          * space character.).          * The option is a<code>boolean</code> type.          * @group consumer          */
DECL|method|urlDecodeHeaders (boolean urlDecodeHeaders)
specifier|default
name|SparkEndpointBuilder
name|urlDecodeHeaders
parameter_list|(
name|boolean
name|urlDecodeHeaders
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"urlDecodeHeaders"
argument_list|,
name|urlDecodeHeaders
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If this option is enabled, then during binding from Spark to Camel          * Message then the header values will be URL decoded (eg %20 will be a          * space character.).          * The option will be converted to a<code>boolean</code> type.          * @group consumer          */
DECL|method|urlDecodeHeaders (String urlDecodeHeaders)
specifier|default
name|SparkEndpointBuilder
name|urlDecodeHeaders
parameter_list|(
name|String
name|urlDecodeHeaders
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"urlDecodeHeaders"
argument_list|,
name|urlDecodeHeaders
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Spark Rest component.      */
DECL|interface|AdvancedSparkEndpointBuilder
specifier|public
specifier|static
interface|interface
name|AdvancedSparkEndpointBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|SparkEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|SparkEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedSparkEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedSparkEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether or not the consumer should try to find a target consumer by          * matching the URI prefix if no exact match is found.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|matchOnUriPrefix ( boolean matchOnUriPrefix)
specifier|default
name|AdvancedSparkEndpointBuilder
name|matchOnUriPrefix
parameter_list|(
name|boolean
name|matchOnUriPrefix
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"matchOnUriPrefix"
argument_list|,
name|matchOnUriPrefix
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether or not the consumer should try to find a target consumer by          * matching the URI prefix if no exact match is found.          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|matchOnUriPrefix ( String matchOnUriPrefix)
specifier|default
name|AdvancedSparkEndpointBuilder
name|matchOnUriPrefix
parameter_list|(
name|String
name|matchOnUriPrefix
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"matchOnUriPrefix"
argument_list|,
name|matchOnUriPrefix
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom SparkBinding to map to/from Camel message.          * The option is a          *<code>org.apache.camel.component.sparkrest.SparkBinding</code> type.          * @group advanced          */
DECL|method|sparkBinding (Object sparkBinding)
specifier|default
name|AdvancedSparkEndpointBuilder
name|sparkBinding
parameter_list|(
name|Object
name|sparkBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"sparkBinding"
argument_list|,
name|sparkBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom SparkBinding to map to/from Camel message.          * The option will be converted to a          *<code>org.apache.camel.component.sparkrest.SparkBinding</code> type.          * @group advanced          */
DECL|method|sparkBinding (String sparkBinding)
specifier|default
name|AdvancedSparkEndpointBuilder
name|sparkBinding
parameter_list|(
name|String
name|sparkBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"sparkBinding"
argument_list|,
name|sparkBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedSparkEndpointBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous (String synchronous)
specifier|default
name|AdvancedSparkEndpointBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * The spark-rest component is used for hosting REST services which has been      * defined using Camel rest-dsl. Creates a builder to build endpoints for      * the Spark Rest component.      */
DECL|method|spark (String path)
specifier|default
name|SparkEndpointBuilder
name|spark
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|SparkEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|SparkEndpointBuilder
implements|,
name|AdvancedSparkEndpointBuilder
block|{
specifier|public
name|SparkEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"spark-rest"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|SparkEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

