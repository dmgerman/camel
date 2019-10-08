begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.dataformat
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|dataformat
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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
name|model
operator|.
name|DataFormatDefinition
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
comment|/**  * The Bindy data format is used for working with flat payloads (such as CSV,  * delimited, fixed length formats, or FIX messages).  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|firstVersion
operator|=
literal|"2.0.0"
argument_list|,
name|label
operator|=
literal|"dataformat,transformation,csv"
argument_list|,
name|title
operator|=
literal|"Bindy"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"bindy"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|BindyDataFormat
specifier|public
class|class
name|BindyDataFormat
extends|extends
name|DataFormatDefinition
block|{
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|type
specifier|private
name|BindyType
name|type
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|classType
specifier|private
name|String
name|classType
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|locale
specifier|private
name|String
name|locale
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|unwrapSingleInstance
specifier|private
name|Boolean
name|unwrapSingleInstance
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
DECL|field|allowEmptyStream
specifier|private
name|Boolean
name|allowEmptyStream
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|clazz
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
decl_stmt|;
DECL|method|BindyDataFormat ()
specifier|public
name|BindyDataFormat
parameter_list|()
block|{
name|super
argument_list|(
literal|"bindy"
argument_list|)
expr_stmt|;
block|}
DECL|method|getType ()
specifier|public
name|BindyType
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
comment|/**      * Whether to use Csv, Fixed, or KeyValue.      */
DECL|method|setType (BindyType type)
specifier|public
name|void
name|setType
parameter_list|(
name|BindyType
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
DECL|method|getClassTypeAsString ()
specifier|public
name|String
name|getClassTypeAsString
parameter_list|()
block|{
return|return
name|classType
return|;
block|}
comment|/**      * Name of model class to use.      */
DECL|method|setClassTypeAsString (String classType)
specifier|public
name|void
name|setClassTypeAsString
parameter_list|(
name|String
name|classType
parameter_list|)
block|{
name|this
operator|.
name|classType
operator|=
name|classType
expr_stmt|;
block|}
comment|/**      * Name of model class to use.      */
DECL|method|setClassType (Class<?> classType)
specifier|public
name|void
name|setClassType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|classType
parameter_list|)
block|{
name|this
operator|.
name|clazz
operator|=
name|classType
expr_stmt|;
block|}
DECL|method|getClassType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getClassType
parameter_list|()
block|{
return|return
name|clazz
return|;
block|}
DECL|method|getLocale ()
specifier|public
name|String
name|getLocale
parameter_list|()
block|{
return|return
name|locale
return|;
block|}
comment|/**      * To configure a default locale to use, such as<tt>us</tt> for united      * states.      *<p/>      * To use the JVM platform default locale then use the name<tt>default</tt>      */
DECL|method|setLocale (String locale)
specifier|public
name|void
name|setLocale
parameter_list|(
name|String
name|locale
parameter_list|)
block|{
name|this
operator|.
name|locale
operator|=
name|locale
expr_stmt|;
block|}
DECL|method|getUnwrapSingleInstance ()
specifier|public
name|Boolean
name|getUnwrapSingleInstance
parameter_list|()
block|{
return|return
name|unwrapSingleInstance
return|;
block|}
comment|/**      * When unmarshalling should a single instance be unwrapped and returned      * instead of wrapped in a<tt>java.util.List</tt>.      */
DECL|method|setUnwrapSingleInstance (Boolean unwrapSingleInstance)
specifier|public
name|void
name|setUnwrapSingleInstance
parameter_list|(
name|Boolean
name|unwrapSingleInstance
parameter_list|)
block|{
name|this
operator|.
name|unwrapSingleInstance
operator|=
name|unwrapSingleInstance
expr_stmt|;
block|}
DECL|method|getAllowEmptyStream ()
specifier|public
name|Boolean
name|getAllowEmptyStream
parameter_list|()
block|{
return|return
name|allowEmptyStream
return|;
block|}
comment|/**    * Whether to allow empty streams in the unmarshal process. If true, no    * exception will be thrown when a body without records is provided.    */
DECL|method|setAllowEmptyStream (Boolean allowEmptyStream)
specifier|public
name|void
name|setAllowEmptyStream
parameter_list|(
name|Boolean
name|allowEmptyStream
parameter_list|)
block|{
name|this
operator|.
name|allowEmptyStream
operator|=
name|allowEmptyStream
expr_stmt|;
block|}
block|}
end_class

end_unit

