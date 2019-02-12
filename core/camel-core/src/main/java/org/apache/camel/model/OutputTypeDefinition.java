begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
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

begin_comment
comment|/**  * Set the expected data type of the output message. If the actual message type is different at runtime,  * camel look for a required {@link Transformer} and apply if exists. If validate attribute is true  * then camel applies {@link Validator} as well.  * Type name consists of two parts, 'scheme' and 'name' connected with ':'. For Java type 'name'  * is a fully qualified class name. For example {@code java:java.lang.String}, {@code json:ABCOrder}.  * It's also possible to specify only scheme part, so that it works like a wildcard. If only 'xml'  * is specified, all the XML message matches. It's handy to add only one transformer/validator  * for all the XML-Java transformation.  *   * @see {@link InputTypeDefinition} {@link Transformer} {@link Validator}  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"configuration"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"outputType"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|OutputTypeDefinition
specifier|public
class|class
name|OutputTypeDefinition
extends|extends
name|OptionalIdentifiedDefinition
argument_list|<
name|OutputTypeDefinition
argument_list|>
block|{
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|urn
specifier|private
name|String
name|urn
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|validate
specifier|private
name|Boolean
name|validate
init|=
literal|false
decl_stmt|;
DECL|method|OutputTypeDefinition ()
specifier|public
name|OutputTypeDefinition
parameter_list|()
block|{     }
comment|/**      * Get output type URN.      * @return output type URN      */
DECL|method|getUrn ()
specifier|public
name|String
name|getUrn
parameter_list|()
block|{
return|return
name|urn
return|;
block|}
comment|/**      * Set output type URN.      * @param urn output type URN      * @return this OutputTypeDefinition instance      */
DECL|method|setUrn (String urn)
specifier|public
name|void
name|setUrn
parameter_list|(
name|String
name|urn
parameter_list|)
block|{
name|this
operator|.
name|urn
operator|=
name|urn
expr_stmt|;
block|}
comment|/**      * Set output type via Java Class.      * @param clazz Java Class      */
DECL|method|setJavaClass (Class<?> clazz)
specifier|public
name|void
name|setJavaClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
name|this
operator|.
name|urn
operator|=
literal|"java:"
operator|+
name|clazz
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
comment|/**      * Get if validation is required for this output type.      * @return true if validate      */
DECL|method|isValidate ()
specifier|public
name|boolean
name|isValidate
parameter_list|()
block|{
return|return
name|this
operator|.
name|validate
return|;
block|}
comment|/**      * Set if validation is required for this output type.      * @param validate true if validate      */
DECL|method|setValidate (boolean validate)
specifier|public
name|void
name|setValidate
parameter_list|(
name|boolean
name|validate
parameter_list|)
block|{
name|this
operator|.
name|validate
operator|=
name|validate
expr_stmt|;
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
literal|"outputType["
operator|+
name|urn
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
return|return
literal|"outputType"
return|;
block|}
annotation|@
name|Override
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
return|return
literal|"outputType["
operator|+
name|urn
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit
