begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api.dto.bulk
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|bulk
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
name|XmlElement
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
name|XmlSchemaType
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
name|XmlType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|datatype
operator|.
name|XMLGregorianCalendar
import|;
end_import

begin_comment
comment|/**  *<p>Java class for BatchInfo complex type.  *<p/>  *<p>The following schema fragment specifies the expected content contained within this class.  *<p/>  *<pre>  *&lt;complexType name="BatchInfo">  *&lt;complexContent>  *&lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">  *&lt;sequence>  *&lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string"/>  *&lt;element name="jobId" type="{http://www.w3.org/2001/XMLSchema}string"/>  *&lt;element name="state" type="{http://www.force.com/2009/06/asyncapi/dataload}BatchStateEnum"/>  *&lt;element name="stateMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>  *&lt;element name="createdDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>  *&lt;element name="systemModstamp" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>  *&lt;element name="numberRecordsProcessed" type="{http://www.w3.org/2001/XMLSchema}int"/>  *&lt;element name="numberRecordsFailed" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>  *&lt;element name="totalProcessingTime" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>  *&lt;element name="apiActiveProcessingTime" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>  *&lt;element name="apexProcessingTime" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>  *&lt;/sequence>  *&lt;/restriction>  *&lt;/complexContent>  *&lt;/complexType>  *</pre>  */
end_comment

begin_class
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"BatchInfo"
argument_list|,
name|propOrder
operator|=
block|{
literal|"id"
block|,
literal|"jobId"
block|,
literal|"state"
block|,
literal|"stateMessage"
block|,
literal|"createdDate"
block|,
literal|"systemModstamp"
block|,
literal|"numberRecordsProcessed"
block|,
literal|"numberRecordsFailed"
block|,
literal|"totalProcessingTime"
block|,
literal|"apiActiveProcessingTime"
block|,
literal|"apexProcessingTime"
block|}
argument_list|)
DECL|class|BatchInfo
specifier|public
class|class
name|BatchInfo
block|{
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|id
specifier|protected
name|String
name|id
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|jobId
specifier|protected
name|String
name|jobId
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|state
specifier|protected
name|BatchStateEnum
name|state
decl_stmt|;
DECL|field|stateMessage
specifier|protected
name|String
name|stateMessage
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|true
argument_list|)
annotation|@
name|XmlSchemaType
argument_list|(
name|name
operator|=
literal|"dateTime"
argument_list|)
DECL|field|createdDate
specifier|protected
name|XMLGregorianCalendar
name|createdDate
decl_stmt|;
annotation|@
name|XmlSchemaType
argument_list|(
name|name
operator|=
literal|"dateTime"
argument_list|)
DECL|field|systemModstamp
specifier|protected
name|XMLGregorianCalendar
name|systemModstamp
decl_stmt|;
DECL|field|numberRecordsProcessed
specifier|protected
name|int
name|numberRecordsProcessed
decl_stmt|;
DECL|field|numberRecordsFailed
specifier|protected
name|Integer
name|numberRecordsFailed
decl_stmt|;
DECL|field|totalProcessingTime
specifier|protected
name|Long
name|totalProcessingTime
decl_stmt|;
DECL|field|apiActiveProcessingTime
specifier|protected
name|Long
name|apiActiveProcessingTime
decl_stmt|;
DECL|field|apexProcessingTime
specifier|protected
name|Long
name|apexProcessingTime
decl_stmt|;
comment|/**      * Gets the value of the id property.      *      * @return possible object is      *         {@link String }      */
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
comment|/**      * Sets the value of the id property.      *      * @param value allowed object is      *              {@link String }      */
DECL|method|setId (String value)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the jobId property.      *      * @return possible object is      *         {@link String }      */
DECL|method|getJobId ()
specifier|public
name|String
name|getJobId
parameter_list|()
block|{
return|return
name|jobId
return|;
block|}
comment|/**      * Sets the value of the jobId property.      *      * @param value allowed object is      *              {@link String }      */
DECL|method|setJobId (String value)
specifier|public
name|void
name|setJobId
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|jobId
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the state property.      *      * @return possible object is      *         {@link BatchStateEnum }      */
DECL|method|getState ()
specifier|public
name|BatchStateEnum
name|getState
parameter_list|()
block|{
return|return
name|state
return|;
block|}
comment|/**      * Sets the value of the state property.      *      * @param value allowed object is      *              {@link BatchStateEnum }      */
DECL|method|setState (BatchStateEnum value)
specifier|public
name|void
name|setState
parameter_list|(
name|BatchStateEnum
name|value
parameter_list|)
block|{
name|this
operator|.
name|state
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the stateMessage property.      *      * @return possible object is      *         {@link String }      */
DECL|method|getStateMessage ()
specifier|public
name|String
name|getStateMessage
parameter_list|()
block|{
return|return
name|stateMessage
return|;
block|}
comment|/**      * Sets the value of the stateMessage property.      *      * @param value allowed object is      *              {@link String }      */
DECL|method|setStateMessage (String value)
specifier|public
name|void
name|setStateMessage
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|stateMessage
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the createdDate property.      *      * @return possible object is      *         {@link javax.xml.datatype.XMLGregorianCalendar }      */
DECL|method|getCreatedDate ()
specifier|public
name|XMLGregorianCalendar
name|getCreatedDate
parameter_list|()
block|{
return|return
name|createdDate
return|;
block|}
comment|/**      * Sets the value of the createdDate property.      *      * @param value allowed object is      *              {@link javax.xml.datatype.XMLGregorianCalendar }      */
DECL|method|setCreatedDate (XMLGregorianCalendar value)
specifier|public
name|void
name|setCreatedDate
parameter_list|(
name|XMLGregorianCalendar
name|value
parameter_list|)
block|{
name|this
operator|.
name|createdDate
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the systemModstamp property.      *      * @return possible object is      *         {@link javax.xml.datatype.XMLGregorianCalendar }      */
DECL|method|getSystemModstamp ()
specifier|public
name|XMLGregorianCalendar
name|getSystemModstamp
parameter_list|()
block|{
return|return
name|systemModstamp
return|;
block|}
comment|/**      * Sets the value of the systemModstamp property.      *      * @param value allowed object is      *              {@link javax.xml.datatype.XMLGregorianCalendar }      */
DECL|method|setSystemModstamp (XMLGregorianCalendar value)
specifier|public
name|void
name|setSystemModstamp
parameter_list|(
name|XMLGregorianCalendar
name|value
parameter_list|)
block|{
name|this
operator|.
name|systemModstamp
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the numberRecordsProcessed property.      */
DECL|method|getNumberRecordsProcessed ()
specifier|public
name|int
name|getNumberRecordsProcessed
parameter_list|()
block|{
return|return
name|numberRecordsProcessed
return|;
block|}
comment|/**      * Sets the value of the numberRecordsProcessed property.      */
DECL|method|setNumberRecordsProcessed (int value)
specifier|public
name|void
name|setNumberRecordsProcessed
parameter_list|(
name|int
name|value
parameter_list|)
block|{
name|this
operator|.
name|numberRecordsProcessed
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the numberRecordsFailed property.      *      * @return possible object is      *         {@link Integer }      */
DECL|method|getNumberRecordsFailed ()
specifier|public
name|Integer
name|getNumberRecordsFailed
parameter_list|()
block|{
return|return
name|numberRecordsFailed
return|;
block|}
comment|/**      * Sets the value of the numberRecordsFailed property.      *      * @param value allowed object is      *              {@link Integer }      */
DECL|method|setNumberRecordsFailed (Integer value)
specifier|public
name|void
name|setNumberRecordsFailed
parameter_list|(
name|Integer
name|value
parameter_list|)
block|{
name|this
operator|.
name|numberRecordsFailed
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the totalProcessingTime property.      *      * @return possible object is      *         {@link Long }      */
DECL|method|getTotalProcessingTime ()
specifier|public
name|Long
name|getTotalProcessingTime
parameter_list|()
block|{
return|return
name|totalProcessingTime
return|;
block|}
comment|/**      * Sets the value of the totalProcessingTime property.      *      * @param value allowed object is      *              {@link Long }      */
DECL|method|setTotalProcessingTime (Long value)
specifier|public
name|void
name|setTotalProcessingTime
parameter_list|(
name|Long
name|value
parameter_list|)
block|{
name|this
operator|.
name|totalProcessingTime
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the apiActiveProcessingTime property.      *      * @return possible object is      *         {@link Long }      */
DECL|method|getApiActiveProcessingTime ()
specifier|public
name|Long
name|getApiActiveProcessingTime
parameter_list|()
block|{
return|return
name|apiActiveProcessingTime
return|;
block|}
comment|/**      * Sets the value of the apiActiveProcessingTime property.      *      * @param value allowed object is      *              {@link Long }      */
DECL|method|setApiActiveProcessingTime (Long value)
specifier|public
name|void
name|setApiActiveProcessingTime
parameter_list|(
name|Long
name|value
parameter_list|)
block|{
name|this
operator|.
name|apiActiveProcessingTime
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the value of the apexProcessingTime property.      *      * @return possible object is      *         {@link Long }      */
DECL|method|getApexProcessingTime ()
specifier|public
name|Long
name|getApexProcessingTime
parameter_list|()
block|{
return|return
name|apexProcessingTime
return|;
block|}
comment|/**      * Sets the value of the apexProcessingTime property.      *      * @param value allowed object is      *              {@link Long }      */
DECL|method|setApexProcessingTime (Long value)
specifier|public
name|void
name|setApexProcessingTime
parameter_list|(
name|Long
name|value
parameter_list|)
block|{
name|this
operator|.
name|apexProcessingTime
operator|=
name|value
expr_stmt|;
block|}
block|}
end_class

end_unit

