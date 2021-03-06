begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Component
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
name|Consumer
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
name|Processor
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
name|Producer
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
name|Metadata
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
name|UriEndpoint
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
name|UriPath
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
name|support
operator|.
name|DefaultEndpoint
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
name|StringHelper
import|;
end_import

begin_import
import|import
name|spark
operator|.
name|route
operator|.
name|HttpMethod
import|;
end_import

begin_comment
comment|/**  * The spark-rest component is used for hosting REST services which has been defined using Camel rest-dsl.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.14.0"
argument_list|,
name|scheme
operator|=
literal|"spark-rest"
argument_list|,
name|title
operator|=
literal|"Spark Rest"
argument_list|,
name|syntax
operator|=
literal|"spark-rest:verb:path"
argument_list|,
name|consumerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"rest"
argument_list|)
DECL|class|SparkEndpoint
specifier|public
class|class
name|SparkEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriPath
argument_list|(
name|enums
operator|=
literal|"get,post,put,patch,delete,head,trace,connect,options"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|verb
specifier|private
name|String
name|verb
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|path
specifier|private
name|String
name|path
decl_stmt|;
annotation|@
name|UriParam
DECL|field|accept
specifier|private
name|String
name|accept
decl_stmt|;
annotation|@
name|UriParam
DECL|field|sparkConfiguration
specifier|private
name|SparkConfiguration
name|sparkConfiguration
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|sparkBinding
specifier|private
name|SparkBinding
name|sparkBinding
decl_stmt|;
DECL|method|SparkEndpoint (String endpointUri, Component component)
specifier|public
name|SparkEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
DECL|method|getSparkConfiguration ()
specifier|public
name|SparkConfiguration
name|getSparkConfiguration
parameter_list|()
block|{
return|return
name|sparkConfiguration
return|;
block|}
comment|/**      * To use the SparkConfiguration      */
DECL|method|setSparkConfiguration (SparkConfiguration sparkConfiguration)
specifier|public
name|void
name|setSparkConfiguration
parameter_list|(
name|SparkConfiguration
name|sparkConfiguration
parameter_list|)
block|{
name|this
operator|.
name|sparkConfiguration
operator|=
name|sparkConfiguration
expr_stmt|;
block|}
DECL|method|getSparkBinding ()
specifier|public
name|SparkBinding
name|getSparkBinding
parameter_list|()
block|{
return|return
name|sparkBinding
return|;
block|}
comment|/**      * To use a custom SparkBinding to map to/from Camel message.      */
DECL|method|setSparkBinding (SparkBinding sparkBinding)
specifier|public
name|void
name|setSparkBinding
parameter_list|(
name|SparkBinding
name|sparkBinding
parameter_list|)
block|{
name|this
operator|.
name|sparkBinding
operator|=
name|sparkBinding
expr_stmt|;
block|}
DECL|method|getVerb ()
specifier|public
name|String
name|getVerb
parameter_list|()
block|{
return|return
name|verb
return|;
block|}
comment|/**      * get, post, put, patch, delete, head, trace, connect, or options.      */
DECL|method|setVerb (String verb)
specifier|public
name|void
name|setVerb
parameter_list|(
name|String
name|verb
parameter_list|)
block|{
name|this
operator|.
name|verb
operator|=
name|verb
expr_stmt|;
block|}
DECL|method|getPath ()
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|path
return|;
block|}
comment|/**      * The content path which support Spark syntax.      */
DECL|method|setPath (String path)
specifier|public
name|void
name|setPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
block|}
DECL|method|getAccept ()
specifier|public
name|String
name|getAccept
parameter_list|()
block|{
return|return
name|accept
return|;
block|}
comment|/**      * Accept type such as: 'text/xml', or 'application/json'. By default we accept all kinds of types.      */
DECL|method|setAccept (String accept)
specifier|public
name|void
name|setAccept
parameter_list|(
name|String
name|accept
parameter_list|)
block|{
name|this
operator|.
name|accept
operator|=
name|accept
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Producer not supported"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|CamelSparkRoute
name|route
init|=
operator|new
name|CamelSparkRoute
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|Consumer
name|consumer
init|=
operator|new
name|SparkConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|route
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|verb
argument_list|,
literal|"verb"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|path
argument_list|,
literal|"path"
argument_list|,
name|this
argument_list|)
expr_stmt|;
comment|// verb must be supported by Spark and lets convert to the actual name
name|HttpMethod
name|method
init|=
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|HttpMethod
operator|.
name|class
argument_list|,
name|verb
argument_list|)
decl_stmt|;
name|verb
operator|=
name|method
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|SparkComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|SparkComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
block|}
end_class

end_unit

