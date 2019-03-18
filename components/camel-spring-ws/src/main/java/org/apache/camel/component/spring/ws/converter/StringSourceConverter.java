begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.ws.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|ws
operator|.
name|converter
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
name|StringSource
import|;
end_import

begin_comment
comment|/**  * A helper class to transform to and from {@link org.springframework.xml.transform.StringSource} implementations  * available in both Camel and Spring Webservices.  *<p/>  * Rationale: most of the time this converter will not be used since both Camel  * and Spring-WS use the {@Source} interface abstraction. There is  * however a chance that you may end up with incompatible {@link org.springframework.xml.transform.StringSource}  * implementations, this converter handles these (corner)cases.  *<p/>  * Note that conversion options are limited by Spring's {@link org.springframework.xml.transform.StringSource}  * since it's the most simple one. It has just one constructor that accepts a  * String as input.  */
end_comment

begin_class
annotation|@
name|Converter
argument_list|(
name|loader
operator|=
literal|true
argument_list|)
DECL|class|StringSourceConverter
specifier|public
specifier|final
class|class
name|StringSourceConverter
block|{
DECL|method|StringSourceConverter ()
specifier|private
name|StringSourceConverter
parameter_list|()
block|{     }
comment|/**      * Converts a Spring-WS {@link org.springframework.xml.transform.StringSource}      * to a Camel {@link org.apache.camel.converter.jaxp.StringSource}      */
annotation|@
name|Converter
DECL|method|toStringSourceFromSpring (org.springframework.xml.transform.StringSource springStringSource)
specifier|public
specifier|static
name|StringSource
name|toStringSourceFromSpring
parameter_list|(
name|org
operator|.
name|springframework
operator|.
name|xml
operator|.
name|transform
operator|.
name|StringSource
name|springStringSource
parameter_list|)
block|{
return|return
operator|new
name|StringSource
argument_list|(
name|springStringSource
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Converts a Camel {@link org.apache.camel.converter.jaxp.StringSource}      * to a Spring-WS {@link org.springframework.xml.transform.StringSource}      */
annotation|@
name|Converter
DECL|method|toStringSourceFromCamel (StringSource camelStringSource)
specifier|public
specifier|static
name|org
operator|.
name|springframework
operator|.
name|xml
operator|.
name|transform
operator|.
name|StringSource
name|toStringSourceFromCamel
parameter_list|(
name|StringSource
name|camelStringSource
parameter_list|)
block|{
return|return
operator|new
name|org
operator|.
name|springframework
operator|.
name|xml
operator|.
name|transform
operator|.
name|StringSource
argument_list|(
name|camelStringSource
operator|.
name|getText
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

