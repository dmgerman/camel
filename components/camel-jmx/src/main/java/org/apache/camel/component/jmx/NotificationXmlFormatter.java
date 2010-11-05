begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jmx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jmx
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
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
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|GregorianCalendar
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|locks
operator|.
name|Lock
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|locks
operator|.
name|ReentrantLock
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|AttributeChangeNotification
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServerNotification
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|Notification
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|monitor
operator|.
name|MonitorNotification
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|relation
operator|.
name|RelationNotification
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|remote
operator|.
name|JMXConnectionNotification
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|timer
operator|.
name|TimerNotification
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
name|JAXBContext
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
name|JAXBException
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
name|Marshaller
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
name|DatatypeConfigurationException
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
name|DatatypeFactory
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
name|component
operator|.
name|jmx
operator|.
name|jaxb
operator|.
name|NotificationEventType
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
name|component
operator|.
name|jmx
operator|.
name|jaxb
operator|.
name|ObjectFactory
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
name|component
operator|.
name|jmx
operator|.
name|jaxb
operator|.
name|ObjectNamesType
import|;
end_import

begin_comment
comment|/**  * Converts the Notification into an XML stream.  *  * @author markford  */
end_comment

begin_class
DECL|class|NotificationXmlFormatter
specifier|public
class|class
name|NotificationXmlFormatter
block|{
DECL|field|mDatatypeFactory
specifier|private
name|DatatypeFactory
name|mDatatypeFactory
decl_stmt|;
DECL|field|mMarshaller
specifier|private
name|Marshaller
name|mMarshaller
decl_stmt|;
DECL|field|mMarshallerLock
specifier|private
name|Lock
name|mMarshallerLock
init|=
operator|new
name|ReentrantLock
argument_list|(
literal|false
argument_list|)
decl_stmt|;
DECL|field|mObjectFactory
specifier|private
name|ObjectFactory
name|mObjectFactory
init|=
operator|new
name|ObjectFactory
argument_list|()
decl_stmt|;
DECL|method|format (Notification aNotification)
specifier|public
name|String
name|format
parameter_list|(
name|Notification
name|aNotification
parameter_list|)
throws|throws
name|NotificationFormatException
block|{
name|NotificationEventType
name|jaxb
init|=
literal|null
decl_stmt|;
name|boolean
name|wrap
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|aNotification
operator|instanceof
name|AttributeChangeNotification
condition|)
block|{
name|AttributeChangeNotification
name|ac
init|=
operator|(
name|AttributeChangeNotification
operator|)
name|aNotification
decl_stmt|;
name|jaxb
operator|=
name|mObjectFactory
operator|.
name|createAttributeChangeNotification
argument_list|()
operator|.
name|withAttributeName
argument_list|(
name|ac
operator|.
name|getAttributeName
argument_list|()
argument_list|)
operator|.
name|withAttributeType
argument_list|(
name|ac
operator|.
name|getAttributeType
argument_list|()
argument_list|)
operator|.
name|withNewValue
argument_list|(
name|ac
operator|.
name|getNewValue
argument_list|()
operator|==
literal|null
condition|?
literal|null
else|:
name|String
operator|.
name|valueOf
argument_list|(
name|ac
operator|.
name|getNewValue
argument_list|()
argument_list|)
argument_list|)
operator|.
name|withOldValue
argument_list|(
name|ac
operator|.
name|getOldValue
argument_list|()
operator|==
literal|null
condition|?
literal|null
else|:
name|String
operator|.
name|valueOf
argument_list|(
name|ac
operator|.
name|getOldValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|aNotification
operator|instanceof
name|JMXConnectionNotification
condition|)
block|{
name|jaxb
operator|=
name|mObjectFactory
operator|.
name|createJMXConnectionNotification
argument_list|()
operator|.
name|withConnectionId
argument_list|(
operator|(
operator|(
name|JMXConnectionNotification
operator|)
name|aNotification
operator|)
operator|.
name|getConnectionId
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|aNotification
operator|instanceof
name|MBeanServerNotification
condition|)
block|{
name|jaxb
operator|=
name|mObjectFactory
operator|.
name|createMBeanServerNotification
argument_list|()
operator|.
name|withMBeanName
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
operator|(
operator|(
name|MBeanServerNotification
operator|)
name|aNotification
operator|)
operator|.
name|getMBeanName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|aNotification
operator|instanceof
name|MonitorNotification
condition|)
block|{
name|MonitorNotification
name|mn
init|=
operator|(
name|MonitorNotification
operator|)
name|aNotification
decl_stmt|;
name|jaxb
operator|=
name|mObjectFactory
operator|.
name|createMonitorNotification
argument_list|()
operator|.
name|withDerivedGauge
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|mn
operator|.
name|getDerivedGauge
argument_list|()
argument_list|)
argument_list|)
operator|.
name|withObservedAttribute
argument_list|(
name|mn
operator|.
name|getObservedAttribute
argument_list|()
argument_list|)
operator|.
name|withObservedObject
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|mn
operator|.
name|getObservedObject
argument_list|()
argument_list|)
argument_list|)
operator|.
name|withTrigger
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|mn
operator|.
name|getTrigger
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|aNotification
operator|instanceof
name|RelationNotification
condition|)
block|{
name|RelationNotification
name|rn
init|=
operator|(
name|RelationNotification
operator|)
name|aNotification
decl_stmt|;
name|jaxb
operator|=
name|mObjectFactory
operator|.
name|createRelationNotification
argument_list|()
operator|.
name|withObjectName
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|rn
operator|.
name|getObjectName
argument_list|()
argument_list|)
argument_list|)
operator|.
name|withRelationId
argument_list|(
name|rn
operator|.
name|getRelationId
argument_list|()
argument_list|)
operator|.
name|withRelationTypeName
argument_list|(
name|rn
operator|.
name|getRelationTypeName
argument_list|()
argument_list|)
operator|.
name|withRoleName
argument_list|(
name|rn
operator|.
name|getRoleName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|rn
operator|.
name|getNewRoleValue
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ObjectNamesType
name|ont
init|=
name|toObjectNamesType
argument_list|(
name|rn
operator|.
name|getNewRoleValue
argument_list|()
argument_list|)
decl_stmt|;
operator|(
operator|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jmx
operator|.
name|jaxb
operator|.
name|RelationNotification
operator|)
name|jaxb
operator|)
operator|.
name|withNewRoleValue
argument_list|(
name|ont
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|rn
operator|.
name|getOldRoleValue
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ObjectNamesType
name|ont
init|=
name|toObjectNamesType
argument_list|(
name|rn
operator|.
name|getOldRoleValue
argument_list|()
argument_list|)
decl_stmt|;
operator|(
operator|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jmx
operator|.
name|jaxb
operator|.
name|RelationNotification
operator|)
name|jaxb
operator|)
operator|.
name|withOldRoleValue
argument_list|(
name|ont
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|rn
operator|.
name|getMBeansToUnregister
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ObjectNamesType
name|ont
init|=
name|toObjectNamesType
argument_list|(
name|rn
operator|.
name|getMBeansToUnregister
argument_list|()
argument_list|)
decl_stmt|;
operator|(
operator|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jmx
operator|.
name|jaxb
operator|.
name|RelationNotification
operator|)
name|jaxb
operator|)
operator|.
name|withMBeansToUnregister
argument_list|(
name|ont
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|aNotification
operator|instanceof
name|TimerNotification
condition|)
block|{
name|jaxb
operator|=
name|mObjectFactory
operator|.
name|createTimerNotification
argument_list|()
operator|.
name|withNotificationId
argument_list|(
operator|(
operator|(
name|TimerNotification
operator|)
name|aNotification
operator|)
operator|.
name|getNotificationID
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|jaxb
operator|=
name|mObjectFactory
operator|.
name|createNotificationEventType
argument_list|()
expr_stmt|;
name|wrap
operator|=
literal|true
expr_stmt|;
block|}
comment|// add all of the common properties
name|jaxb
operator|.
name|withMessage
argument_list|(
name|aNotification
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|withSequence
argument_list|(
name|aNotification
operator|.
name|getSequenceNumber
argument_list|()
argument_list|)
operator|.
name|withSource
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|aNotification
operator|.
name|getSource
argument_list|()
argument_list|)
argument_list|)
operator|.
name|withTimestamp
argument_list|(
name|aNotification
operator|.
name|getTimeStamp
argument_list|()
argument_list|)
operator|.
name|withType
argument_list|(
name|aNotification
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|aNotification
operator|.
name|getUserData
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|jaxb
operator|.
name|withUserData
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|aNotification
operator|.
name|getUserData
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|DatatypeFactory
name|df
init|=
name|getDatatypeFactory
argument_list|()
decl_stmt|;
name|Date
name|date
init|=
operator|new
name|Date
argument_list|(
name|aNotification
operator|.
name|getTimeStamp
argument_list|()
argument_list|)
decl_stmt|;
name|GregorianCalendar
name|gc
init|=
operator|new
name|GregorianCalendar
argument_list|()
decl_stmt|;
name|gc
operator|.
name|setTime
argument_list|(
name|date
argument_list|)
expr_stmt|;
name|jaxb
operator|.
name|withDateTime
argument_list|(
name|df
operator|.
name|newXMLGregorianCalendar
argument_list|(
name|gc
argument_list|)
argument_list|)
expr_stmt|;
name|Object
name|bean
init|=
name|wrap
condition|?
name|mObjectFactory
operator|.
name|createNotificationEvent
argument_list|(
name|jaxb
argument_list|)
else|:
name|jaxb
decl_stmt|;
name|StringWriter
name|sw
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
try|try
block|{
name|mMarshallerLock
operator|.
name|lock
argument_list|()
expr_stmt|;
name|getMarshaller
argument_list|(
name|mObjectFactory
operator|.
name|getClass
argument_list|()
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|marshal
argument_list|(
name|bean
argument_list|,
name|sw
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|mMarshallerLock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
return|return
name|sw
operator|.
name|toString
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|NotificationFormatException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|DatatypeConfigurationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|NotificationFormatException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|toObjectNamesType (List<ObjectName> objectNameList)
specifier|private
name|ObjectNamesType
name|toObjectNamesType
parameter_list|(
name|List
argument_list|<
name|ObjectName
argument_list|>
name|objectNameList
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|roles
init|=
name|toStringList
argument_list|(
name|objectNameList
argument_list|)
decl_stmt|;
name|ObjectNamesType
name|ont
init|=
name|mObjectFactory
operator|.
name|createObjectNamesType
argument_list|()
decl_stmt|;
name|ont
operator|.
name|withObjectName
argument_list|(
name|roles
argument_list|)
expr_stmt|;
return|return
name|ont
return|;
block|}
DECL|method|getDatatypeFactory ()
specifier|private
name|DatatypeFactory
name|getDatatypeFactory
parameter_list|()
throws|throws
name|DatatypeConfigurationException
block|{
if|if
condition|(
name|mDatatypeFactory
operator|==
literal|null
condition|)
block|{
name|mDatatypeFactory
operator|=
name|DatatypeFactory
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
return|return
name|mDatatypeFactory
return|;
block|}
DECL|method|getMarshaller (String aPackageName)
specifier|private
name|Marshaller
name|getMarshaller
parameter_list|(
name|String
name|aPackageName
parameter_list|)
throws|throws
name|JAXBException
block|{
if|if
condition|(
name|mMarshaller
operator|==
literal|null
condition|)
block|{
name|mMarshaller
operator|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|aPackageName
argument_list|)
operator|.
name|createMarshaller
argument_list|()
expr_stmt|;
block|}
return|return
name|mMarshaller
return|;
block|}
DECL|method|toStringList (List<ObjectName> objectNames)
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|toStringList
parameter_list|(
name|List
argument_list|<
name|ObjectName
argument_list|>
name|objectNames
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|roles
init|=
operator|new
name|ArrayList
argument_list|(
name|objectNames
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|ObjectName
name|on
range|:
name|objectNames
control|)
block|{
name|roles
operator|.
name|add
argument_list|(
name|on
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|roles
return|;
block|}
block|}
end_class

end_unit

