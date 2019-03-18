begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.zipkin.starter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|zipkin
operator|.
name|starter
package|;
end_package

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|condition
operator|.
name|ConditionalOnProperty
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Import
import|;
end_import

begin_comment
comment|/**  * A configuration controller to enable Zipkin via the configuration property.  * Useful to bootstrap Zipkin when not using the {@link CamelZipkin} annotation.  */
end_comment

begin_class
annotation|@
name|Configuration
annotation|@
name|ConditionalOnProperty
argument_list|(
name|value
operator|=
literal|"camel.zipkin.enabled"
argument_list|)
annotation|@
name|Import
argument_list|(
name|ZipkinAutoConfiguration
operator|.
name|class
argument_list|)
DECL|class|ZipkinConditionalAutoConfiguration
specifier|public
class|class
name|ZipkinConditionalAutoConfiguration
block|{ }
end_class

end_unit

