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
name|impl
operator|.
name|DefaultConsumer
import|;
end_import

begin_class
DECL|class|SparkConsumer
specifier|public
class|class
name|SparkConsumer
extends|extends
name|DefaultConsumer
block|{
DECL|field|route
specifier|private
specifier|final
name|CamelSparkRoute
name|route
decl_stmt|;
DECL|field|enableCors
specifier|private
name|boolean
name|enableCors
decl_stmt|;
DECL|method|SparkConsumer (Endpoint endpoint, Processor processor, CamelSparkRoute route)
specifier|public
name|SparkConsumer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|CamelSparkRoute
name|route
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|route
operator|=
name|route
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|SparkEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|SparkEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
DECL|method|isEnableCors ()
specifier|public
name|boolean
name|isEnableCors
parameter_list|()
block|{
return|return
name|enableCors
return|;
block|}
DECL|method|setEnableCors (boolean enableCors)
specifier|public
name|void
name|setEnableCors
parameter_list|(
name|boolean
name|enableCors
parameter_list|)
block|{
name|this
operator|.
name|enableCors
operator|=
name|enableCors
expr_stmt|;
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
name|String
name|verb
init|=
name|getEndpoint
argument_list|()
operator|.
name|getVerb
argument_list|()
decl_stmt|;
name|String
name|path
init|=
name|getEndpoint
argument_list|()
operator|.
name|getPath
argument_list|()
decl_stmt|;
name|String
name|accept
init|=
name|getEndpoint
argument_list|()
operator|.
name|getAccept
argument_list|()
decl_stmt|;
name|boolean
name|matchOnUriPrefix
init|=
name|getEndpoint
argument_list|()
operator|.
name|getSparkConfiguration
argument_list|()
operator|.
name|isMatchOnUriPrefix
argument_list|()
decl_stmt|;
if|if
condition|(
name|accept
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Spark-rest: {}({}) accepting: {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|verb
block|,
name|path
block|,
name|accept
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Spark-rest: {}({})"
argument_list|,
name|verb
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
name|CamelSpark
operator|.
name|spark
argument_list|(
name|verb
argument_list|,
name|path
argument_list|,
name|accept
argument_list|,
name|route
argument_list|)
expr_stmt|;
comment|// special if cors is enabled in rest-dsl then we need a spark-route to trigger cors support
if|if
condition|(
name|enableCors
operator|&&
operator|!
literal|"options"
operator|.
name|equals
argument_list|(
name|verb
argument_list|)
condition|)
block|{
name|CamelSpark
operator|.
name|spark
argument_list|(
literal|"options"
argument_list|,
name|path
argument_list|,
name|accept
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|matchOnUriPrefix
condition|)
block|{
name|CamelSpark
operator|.
name|spark
argument_list|(
name|verb
argument_list|,
name|path
operator|+
literal|"/*"
argument_list|,
name|accept
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

